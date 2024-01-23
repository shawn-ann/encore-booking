package com.lab.dev.shawn.api.admin.concert.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.dto.DropdownOptions;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.admin.concert.vo.SessionRequestVO;
import com.lab.dev.shawn.api.admin.concert.vo.ConcertRequestVO;
import com.lab.dev.shawn.api.entity.Concert;
import com.lab.dev.shawn.api.entity.Inventory;
import com.lab.dev.shawn.api.entity.Session;
import com.lab.dev.shawn.api.entity.TicketCategory;
import com.lab.dev.shawn.api.repository.*;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
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

    public Page<Concert> findByName(int page, int limit, String name) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return concertRepository.findByName(name, pageable);
    }

    public void create(ConcertRequestVO requestVO) {
        Concert concert = new Concert();
        concert.setName(requestVO.getName());
        concert.setStatus(BaseStatus.ACTIVE);

        List<Session> sessionList = new ArrayList<>();
        concert.setSessionList(sessionList);
        List<TicketCategory> ticketCategoryList = new ArrayList<>();
        concert.setTicketCategoryList(ticketCategoryList);

        List<SessionRequestVO> sessionVOList = requestVO.getSessionList();
        sessionVOList.forEach(sessionVO -> {
            Session session = new Session();
            session.setConcert(concert);
            session.setName(sessionVO.getName());
            sessionList.add(session);
        });

        List<ConcertRequestVO.TicketCategoryRequestVO> ticketCategoryVOList = requestVO.getTicketCategoryList();
        ticketCategoryVOList.forEach(ticketCategoryRequestVO -> {
            TicketCategory ticket = new TicketCategory();
            ticket.setConcert(concert);
            ticket.setName(ticketCategoryRequestVO.getName());
            ticket.setDeleted(false);
            ticketCategoryList.add(ticket);
        });
        initInventory(concert);
        concertRepository.save(concert);
    }

    private void initInventory(Concert concert) {
        List<Inventory> inventoryList = new ArrayList<>();
        List<Session> sessionList = concert.getSessionList();
        List<TicketCategory> ticketCategoryList = concert.getTicketCategoryList();
        sessionList.forEach(session -> {
            ticketCategoryList.forEach(ticketCategory -> {
                Inventory inventory = new Inventory();
                inventory.setConcert(concert);
                inventory.setSession(session);
                inventory.setTicketCategory(ticketCategory);
                inventory.setTotalQuantity(0);
                inventory.setRemainingQuantity(0);
                inventory.setUnallocatedQuantity(0);
                inventory.setStatus(BaseStatus.ACTIVE);
                inventoryList.add(inventory);
            });
        });
        concert.setInventoryList(inventoryList);
    }

    public void update(ConcertRequestVO requestVO) throws BaseException {
        Concert concert = concertRepository.findById(requestVO.getId()).get();
        if (concert == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }
        if (concert.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        concert.setName(requestVO.getName());

        List<Session> sessionListInDB = concert.getSessionList();
        List<SessionRequestVO> sessionVOList = requestVO.getSessionList();
        List<Session> sessionList = getUpdatedSessionList(sessionListInDB, sessionVOList, concert);
        concert.setSessionList(sessionList);


        List<TicketCategory> ticketCategoryListInDB = concert.getTicketCategoryList();
        List<ConcertRequestVO.TicketCategoryRequestVO> ticketCategoryVOList = requestVO.getTicketCategoryList();
        List<TicketCategory> ticketCategoryList = getUpdatedTicketCategoryList(ticketCategoryListInDB, ticketCategoryVOList, concert);
        concert.setTicketCategoryList(ticketCategoryList);

        updateInventory(concert);

        concertRepository.save(concert);
    }

    private void updateInventory(Concert concert) {

        List<Inventory> inventoryList = concert.getInventoryList();

        List<Session> sessionList = concert.getSessionList();
        List<TicketCategory> ticketCategoryList = concert.getTicketCategoryList();
        sessionList.forEach(session -> {
            ticketCategoryList.forEach(ticketCategory -> {
                long countInDB = inventoryList.stream().filter(item -> item.getSession().getId() == session.getId() && item.getTicketCategory().getId() == ticketCategory.getId()).count();
                if (countInDB == 0) {
                    Inventory inventory = new Inventory();
                    inventory.setConcert(concert);
                    inventory.setSession(session);
                    inventory.setTicketCategory(ticketCategory);
                    inventory.setTotalQuantity(0);
                    inventory.setRemainingQuantity(0);
                    inventory.setUnallocatedQuantity(0);
                    inventory.setStatus(BaseStatus.ACTIVE);
                    inventoryList.add(inventory);
                }

            });
        });
        concert.setInventoryList(inventoryList);
    }

    private List<Session> getUpdatedSessionList(List<Session> sessionListInDB, List<SessionRequestVO> sessionVOList, Concert concert) throws BaseException {
        List<Session> results = new ArrayList<>();
        List<Long> deletedSessionIds = new ArrayList<>();
        sessionListInDB.forEach(dbItem -> {
            SessionRequestVO filteredVoItem = sessionVOList.stream().filter(voItem -> StringUtils.isNotBlank(voItem.getId()) && dbItem.getId().equals(Long.valueOf(voItem.getId()))).findFirst().orElse(null);
            if (filteredVoItem == null) {
                // Delete
                dbItem.setDeleted(true);
                concert.getInventoryList().stream().filter(temp -> dbItem.getId().equals(temp.getSession().getId())).forEach(temp -> temp.setDeleted(true));
                deletedSessionIds.add(dbItem.getId());
            } else if (Long.valueOf(filteredVoItem.getId()).equals(dbItem)) {
                // Update
                dbItem.setName(filteredVoItem.getName());
            }
            results.add(dbItem);
        });
//        if (!deletedSessionIds.isEmpty()) {
//            List<Inventory> inventories = inventoryRepository.findByTicketCategoryIds(deletedSessionIds);
//            if (!inventories.isEmpty()) {
//                String collectedTicketCategoryNames = inventories.stream().map(item -> item.getTicketCategory().getName()).collect(Collectors.joining(","));
//                throw new BaseException(50008, "请先删除场次[" + collectedTicketCategoryNames + "]对应的库存再删除该记录");
//            }
//        }
        List<SessionRequestVO> needAddVOs = findSourceItemsNotInTargetList(sessionVOList, sessionListInDB, (SessionRequestVO voItem, Session dbItem) -> StringUtils.isNotBlank(voItem.getId()) && Long.valueOf(voItem.getId()).equals(dbItem.getId()));
        needAddVOs.forEach(voItems -> {
            Session session = new Session();
            session.setName(voItems.getName());
            session.setConcert(concert);
            results.add(session);
        });

        return results;
    }

    private List<TicketCategory> getUpdatedTicketCategoryList(List<TicketCategory> ticketCategoryListInDB, List<ConcertRequestVO.TicketCategoryRequestVO> ticketCategoryVOList, Concert concert) throws BaseException {
        List<TicketCategory> results = new ArrayList<>();
        List<Long> deletedCategoryIds = new ArrayList<>();
        ticketCategoryListInDB.forEach(dbItem -> {
            ConcertRequestVO.TicketCategoryRequestVO filteredVoItem = ticketCategoryVOList.stream().filter(voItem -> StringUtils.isNotBlank(voItem.getId()) && dbItem.getId().equals(Long.valueOf(voItem.getId()))).findFirst().orElse(null);
            if (filteredVoItem == null) {
                // Delete
                dbItem.setDeleted(true);
                concert.getInventoryList().stream().filter(temp -> dbItem.getId().equals(temp.getTicketCategory().getId())).forEach(temp -> temp.setDeleted(true));
                deletedCategoryIds.add(dbItem.getId());
            } else if (Long.valueOf(filteredVoItem.getId()).equals(dbItem)) {
                // Update
                dbItem.setName(filteredVoItem.getName());
            }
            results.add(dbItem);
        });
//        if (!deletedCategoryIds.isEmpty()) {
//            List<Inventory> inventories = inventoryRepository.findByTicketCategoryIds(deletedCategoryIds);
//            if (!inventories.isEmpty()) {
//                String collectedTicketCategoryNames = inventories.stream().map(item -> item.getTicketCategory().getName()).collect(Collectors.joining(","));
//                throw new BaseException(50008, "请先删除票档[" + collectedTicketCategoryNames + "]对应的库存再删除该记录");
//            }
//        }

        List<ConcertRequestVO.TicketCategoryRequestVO> needAddVOs = findSourceItemsNotInTargetList(ticketCategoryVOList, ticketCategoryListInDB, (ConcertRequestVO.TicketCategoryRequestVO voItem, TicketCategory dbItem) -> StringUtils.isNotBlank(voItem.getId()) && Long.valueOf(voItem.getId()).equals(dbItem.getId()));
        needAddVOs.forEach(voItems -> {
            TicketCategory ticketCategory = new TicketCategory();
            ticketCategory.setName(voItems.getName());
            ticketCategory.setConcert(concert);
            results.add(ticketCategory);
        });

        return results;
    }

    private static <T, U> List<T> findSourceItemsNotInTargetList(List<T> source, List<U> target, BiPredicate<T, U> func) {
        return source.stream().filter(sourceItem -> target.stream().noneMatch(targetItem -> func.test(sourceItem, targetItem))).collect(Collectors.toList());
    }

    public void delete(Long id) throws BaseException {
        Concert concert = concertRepository.findById(id).get();
        if (concert == null) {
            throw new BaseException(BaseExceptionEnum.NOT_FOUND_MATCH_RECORD);
        }

        concert.getInventoryList().forEach(item -> item.setDeleted(true));
        concert.getSessionList().forEach(item -> item.setDeleted(true));
        concert.getTicketCategoryList().forEach(item -> item.setDeleted(true));

        concert.setDeleted(true);
        concertRepository.save(concert);
    }

    public List<DropdownOptions> findDropdownOptions() {
        return concertRepository.findDropdownOptions();
    }
}
