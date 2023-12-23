package com.lab.dev.shawn.admin.business.session.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.dto.DropdownOptions;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.business.session.vo.SessionRequestVO;
import com.lab.dev.shawn.admin.business.session.vo.SessionResponseVO;
import com.lab.dev.shawn.admin.entity.Concert;
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
public class SessionService {

    private SessionRepository sessionRepository;
    private ConcertRepository concertRepository;
    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Autowired
    public void setConcertRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public Page<SessionResponseVO> findByFilter(int page, int limit, Long concertId) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return sessionRepository.findByFilter(concertId, pageable);
    }

    public void create(SessionRequestVO requestVO) {

        Concert concert = concertRepository.findById(requestVO.getConcertId()).get();

        Session session = new Session();
        session.setConcert(concert);
        session.setName(requestVO.getName());
        session.setStatus(BaseStatus.ACTIVE);
        sessionRepository.save(session);
    }

    public void update(SessionRequestVO requestVO) throws BaseException {
        Session entity = sessionRepository.findById(requestVO.getId()).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        entity.setName(requestVO.getName());
        sessionRepository.save(entity);
    }

    public void delete(Long id) throws BaseException {
        Session entity = sessionRepository.findById(id).get();
        if (entity == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        entity.setDeleted(true);
        sessionRepository.save(entity);
    }

    public List<DropdownOptions> findDropdownOptions(Long concertId) {
        return sessionRepository.findDropdownOptions(concertId);
    }
}