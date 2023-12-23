package com.lab.dev.shawn.admin.business.ticketCategory.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.TicketCategory;
import com.lab.dev.shawn.admin.entity.Concert;
import com.lab.dev.shawn.admin.entity.Inventory;
import com.lab.dev.shawn.admin.repository.TicketCategoryRepository;
import com.lab.dev.shawn.admin.repository.ConcertRepository;
import com.lab.dev.shawn.admin.repository.InventoryRepository;
import com.lab.dev.shawn.admin.business.ticketCategory.vo.TicketCategoryRequestVO;
import com.lab.dev.shawn.admin.business.ticketCategory.vo.TicketCategoryResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketCategoryService {

    private TicketCategoryRepository ticketCategoryRepository;
    private ConcertRepository concertRepository;
    private InventoryRepository inventoryRepository;

    @Autowired
    public void setTicketRepository(TicketCategoryRepository ticketCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
    }
    @Autowired
    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }
    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Page<TicketCategoryResponseVO> findByFilter(int page, int limit, Long concertId) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ticketCategoryRepository.findByFilter(concertId, pageable);
    }

    public void create(TicketCategoryRequestVO requestVO) {

        Concert concert = concertRepository.findById(requestVO.getConcertId()).get();

        TicketCategory ticket = new TicketCategory();
        ticket.setConcert(concert);
        ticket.setName(requestVO.getName());
        ticket.setStatus(BaseStatus.ACTIVE);
        ticketCategoryRepository.save(ticket);
    }

    public void update(TicketCategoryRequestVO requestVO) throws BaseException {
        TicketCategory entity = ticketCategoryRepository.findById(requestVO.getId()).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        entity.setName(requestVO.getName());
        ticketCategoryRepository.save(entity);
    }

    public void delete(Long id) throws BaseException {
        TicketCategory entity = ticketCategoryRepository.findById(id).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        entity.setDeleted(true);
        ticketCategoryRepository.save(entity);
    }

    public List<DropdownOptions> findDropdownOptions(Long concertId) {
        return ticketCategoryRepository.findDropdownOptions(concertId);
    }
}
