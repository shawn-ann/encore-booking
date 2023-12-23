package com.lab.dev.shawn.admin.business.inventory.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.business.inventory.vo.InventoryRequestVO;
import com.lab.dev.shawn.admin.business.inventory.vo.InventoryResponseVO;
import com.lab.dev.shawn.admin.business.inventory.vo.UpdateQuantityRequestVO;
import com.lab.dev.shawn.admin.entity.Concert;
import com.lab.dev.shawn.admin.entity.Inventory;
import com.lab.dev.shawn.admin.entity.Session;
import com.lab.dev.shawn.admin.entity.TicketCategory;
import com.lab.dev.shawn.admin.repository.ConcertRepository;
import com.lab.dev.shawn.admin.repository.InventoryRepository;
import com.lab.dev.shawn.admin.repository.SessionRepository;
import com.lab.dev.shawn.admin.repository.TicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventoryService {

    private ConcertRepository concertRepository;
    private SessionRepository sessionRepository;
    private TicketCategoryRepository ticketCategoryRepository;
    private InventoryRepository inventoryRepository;

    @Autowired
    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setTicketCategoryRepository(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }

    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<InventoryResponseVO> findByFilter(int page, int limit, Long concertId) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return inventoryRepository.findByFilter(concertId, pageable);
    }

    public void create(InventoryRequestVO requestVO) throws BaseException {

        Concert concert = concertRepository.findById(requestVO.getConcertId()).get();
        Session session = sessionRepository.findById(requestVO.getSessionId()).get();
        TicketCategory ticketCategory = ticketCategoryRepository.findById(requestVO.getTicketCategoryId()).get();

        Inventory existsRecord = inventoryRepository.findByConcertIdAndSessionIdAndTicketCategoryId(requestVO.getConcertId(), requestVO.getSessionId(), requestVO.getTicketCategoryId());
        if (existsRecord != null) {
            throw new BaseException(BaseExceptionEnum.DUPLICATED_RECORD);
        }

        Inventory inventory = new Inventory();
        inventory.setConcert(concert);
        inventory.setSession(session);
        inventory.setTicketCategory(ticketCategory);
        inventory.setTotalQuantity(requestVO.getQuantity());
        inventory.setRemainingQuantity(requestVO.getQuantity());
        inventory.setUnallocatedQuantity(requestVO.getQuantity());
        inventory.setStatus(BaseStatus.INACTIVE);
        inventoryRepository.save(inventory);
    }


    public void updateQuantity(UpdateQuantityRequestVO requestVO) throws BaseException {
        Long id = requestVO.getId();

        Inventory entity = inventoryRepository.findById(id).get();
        if (entity.getStatus().equals(BaseStatus.ACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        int quantity = Integer.valueOf(requestVO.getQuantity());
        if ("MINUS".equals(requestVO.getOperation())) {
            quantity = -quantity;
        }
        int rows = inventoryRepository.updateQuantity(id, quantity);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
    }

    public void delete(Long id) throws BaseException {
        Inventory entity = inventoryRepository.findById(id).get();

        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (entity.getStatus().equals(BaseStatus.ACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        entity.setDeleted(true);
        inventoryRepository.save(entity);
    }

    public void inactive(String id) throws BaseException {
        int rows = inventoryRepository.changeStatus(Long.valueOf(id), BaseStatus.ACTIVE, BaseStatus.INACTIVE);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
    }

    public void active(String id) throws BaseException {
        int rows = inventoryRepository.changeStatus(Long.valueOf(id), BaseStatus.INACTIVE, BaseStatus.ACTIVE);
        if (rows != 1) {
            throw new BaseException(BaseExceptionEnum.UPDATE_FAILED);
        }
    }

    public List<DropdownOptions> findDropdownOptions(Long concertId) {
        return inventoryRepository.findDropdownOptions(concertId);
    }
}
