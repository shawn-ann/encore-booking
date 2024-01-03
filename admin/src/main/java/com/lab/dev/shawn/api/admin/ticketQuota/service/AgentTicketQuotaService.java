package com.lab.dev.shawn.api.admin.ticketQuota.service;

import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaQuantityUpdateRequestVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaRequestVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaResponseVO;
import com.lab.dev.shawn.api.admin.ticketQuota.vo.AgentTicketQuotaUpdateRequestVO;
import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.Agent;
import com.lab.dev.shawn.api.entity.AgentTicketQuota;
import com.lab.dev.shawn.api.entity.Inventory;
import com.lab.dev.shawn.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgentTicketQuotaService {


    private AgentTicketQuotaRepository quotaRepository;
    private TicketCategoryRepository ticketCategoryRepository;
    private ConcertRepository concertRepository;
    private InventoryRepository inventoryRepository;
    private AgentRepository agentRepository;

    @Autowired
    public void setTicketRepository(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    @Autowired
    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Autowired
    public void setQuotaRepository(AgentTicketQuotaRepository quotaRepository) {
        this.quotaRepository = quotaRepository;
    }

    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Autowired
    public void setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Page<AgentTicketQuotaResponseVO> findByFilter(int page, int limit, Long concertId, Long agentId) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return quotaRepository.findByFilter(concertId, agentId, pageable);
    }

    public void create(AgentTicketQuotaRequestVO requestVO) throws BaseException {

        Inventory inventory = inventoryRepository.findById(requestVO.getInventoryId()).get();
        if (inventory == null) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
        Agent agent = agentRepository.findById(requestVO.getAgentId()).get();
        if (inventory == null) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }

        AgentTicketQuota existsRecord = agentRepository.findByAgentIdAndInventoryId(agent.getId(), requestVO.getInventoryId());
        if (existsRecord != null) {
            throw new BaseException(BaseExceptionEnum.DUPLICATED_RECORD);
        }

        int rows = inventoryRepository.addUnallocatedQuantity(inventory.getId(), -requestVO.getQuantity());
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UNALLOCATED_QUANTITY_NOT_ENOUGTH);
        }
        AgentTicketQuota entity = new AgentTicketQuota();
        entity.setAgent(agent);
        entity.setInventory(inventory);
        entity.setPrice(requestVO.getPrice());
        entity.setTotalQuantity(requestVO.getQuantity());
        entity.setRemainingQuantity(requestVO.getQuantity());
        entity.setStatus(BaseStatus.INACTIVE);
        quotaRepository.save(entity);

    }

    public void update(AgentTicketQuotaUpdateRequestVO requestVO) throws BaseException {
        Long id = requestVO.getId();
        AgentTicketQuota quota = quotaRepository.findById(id).get();
        if (quota == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (quota.getStatus().equals(BaseStatus.ACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        quota.setPrice(requestVO.getPrice());
        quotaRepository.save(quota);
    }

    public void delete(Long id) throws BaseException {
        AgentTicketQuota entity = quotaRepository.findById(id).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (entity.getStatus().equals(BaseStatus.ACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        entity.setDeleted(true);
        quotaRepository.save(entity);
    }

    public void active(Long id) throws BaseException {

        int rows = quotaRepository.changeStatus(id, BaseStatus.INACTIVE, BaseStatus.ACTIVE);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
    }

    public void inactive(Long id) throws BaseException {

        int rows = quotaRepository.changeStatus(id, BaseStatus.ACTIVE, BaseStatus.INACTIVE);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
    }

    public void quantityUpdate(AgentTicketQuotaQuantityUpdateRequestVO requestVO) throws BaseException {

        Long id = requestVO.getId();
        AgentTicketQuota quota = quotaRepository.findById(id).get();
        if (quota == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (quota.getStatus().equals(BaseStatus.ACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        int quantity = Integer.valueOf(requestVO.getQuantity());
        if ("MINUS".equals(requestVO.getOperation())) {
            quantity = -quantity;
        }
        int rows = inventoryRepository.addUnallocatedQuantity(quota.getInventory().getId(), -quantity);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UNALLOCATED_QUANTITY_NOT_ENOUGTH);
        }
        rows = quotaRepository.updateQuantity(id, quantity);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }

    }
}
