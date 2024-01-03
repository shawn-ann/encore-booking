package com.lab.dev.shawn.api.admin.session.service;

import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.repository.ConcertRepository;
import com.lab.dev.shawn.api.repository.InventoryRepository;
import com.lab.dev.shawn.api.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SessionService {

    private SessionRepository sessionRepository;
    private ConcertRepository concertRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }


    public List<DropdownOptions> findDropdownOptions(Long concertId) {
        return sessionRepository.findDropdownOptions(concertId);
    }
}
