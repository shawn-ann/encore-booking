package com.lab.dev.shawn.admin.business.concert.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.*;
import com.lab.dev.shawn.admin.repository.*;
import com.lab.dev.shawn.admin.business.concert.vo.ConcertRequestVO;
import com.lab.dev.shawn.admin.business.concert.vo.ConcertResponseVO;
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
public class ConcertService {


    private ConcertRepository concertRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;

    @Autowired
    public void setAgentRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public Page<ConcertResponseVO> findByName(int page, int limit, String name) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return concertRepository.findByName(name, pageable);
    }

    public void create(ConcertRequestVO requestVO) {
        Concert entity = new Concert();
        entity.setName(requestVO.getName());
        entity.setStatus(BaseStatus.ACTIVE);
        concertRepository.save(entity);
    }

    public void update(ConcertRequestVO requestVO) throws BaseException {
        Concert entity = concertRepository.findById(requestVO.getId()).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (entity.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        entity.setName(requestVO.getName());
        concertRepository.save(entity);
    }

    public void delete(Long id) throws BaseException {
        Concert entity = concertRepository.findById(id).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (entity.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        List<Session> sessionList = sessionRepository.findByConcertId(id);
        if (!sessionList.isEmpty()) {
            throw new BaseException(50008, "请先删除场次再删除该记录");
        }
        List<TicketCategory> ticketCategoryList = ticketCategoryRepository.findByConcertId(id);

        if (!ticketCategoryList.isEmpty()) {
            throw new BaseException(50008, "请先删除票型再删除该记录");
        }

        entity.setDeleted(true);
        concertRepository.save(entity);
    }

    public List<DropdownOptions> findDropdownOptions() {
        return concertRepository.findDropdownOptions();
    }
}
