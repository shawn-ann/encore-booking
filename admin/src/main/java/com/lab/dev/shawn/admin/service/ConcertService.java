package com.lab.dev.shawn.admin.service;

import com.lab.dev.shawn.admin.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.admin.base.constant.BaseStatus;
import com.lab.dev.shawn.admin.base.exception.BaseException;
import com.lab.dev.shawn.admin.entity.Agent;
import com.lab.dev.shawn.admin.entity.Concert;
import com.lab.dev.shawn.admin.repository.AgentRepository;
import com.lab.dev.shawn.admin.repository.ConcertRepository;
import com.lab.dev.shawn.admin.util.ServiceUtil;
import com.lab.dev.shawn.admin.vo.AgentRequestVO;
import com.lab.dev.shawn.admin.vo.AgentResponseVO;
import com.lab.dev.shawn.admin.vo.ConcertRequestVO;
import com.lab.dev.shawn.admin.vo.ConcertResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {


    private ConcertRepository concertRepository;

    @Autowired
    public void setAgentRepository(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> findById(Long userId) {
        return concertRepository.findById(userId).stream().toList();
    }

    public Page<ConcertResponseVO> findByMobile(int page, int limit, String name) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return concertRepository.findByName(name, pageable);
    }

    public void create(ConcertRequestVO requestVO) {
        Concert entity = new Concert();
        entity.setName(requestVO.getName());
        entity.setStatus(BaseStatus.ACTIVE);
        ServiceUtil.handleCreate(entity);
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
        entity.setStatus(BaseStatus.INACTIVE);
        concertRepository.save(entity);
    }
}
