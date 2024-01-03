package com.lab.dev.shawn.api.admin.ticketCategory.service;

import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.repository.TicketCategoryRepository;
import com.lab.dev.shawn.api.repository.ConcertRepository;
import com.lab.dev.shawn.api.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<DropdownOptions> findDropdownOptions(Long concertId) {
        return ticketCategoryRepository.findDropdownOptions(concertId);
    }
}
