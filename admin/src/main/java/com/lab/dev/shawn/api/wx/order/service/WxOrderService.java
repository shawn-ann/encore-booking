package com.lab.dev.shawn.api.wx.order.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.constant.BaseStatus;
import com.lab.dev.shawn.api.base.constant.OrderStatus;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.*;
import com.lab.dev.shawn.api.repository.AgentRepository;
import com.lab.dev.shawn.api.repository.AgentTicketQuotaRepository;
import com.lab.dev.shawn.api.repository.InventoryRepository;
import com.lab.dev.shawn.api.repository.BookingOrderRepository;
import com.lab.dev.shawn.api.wx.order.dto.CreateOrderResponseDTO;
import com.lab.dev.shawn.api.wx.order.vo.CreateOrderRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxOrderService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private BookingOrderRepository bookingOrderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public CreateOrderResponseDTO create(CreateOrderRequestVO requestVO, Long agentId) throws BaseException {
        int buyerCount = requestVO.getBuyerList().size();
        //查询配额
        AgentTicketQuota agentTicketQuota = agentTicketQuotaRepository.findById(requestVO.getQuotaId()).get();
        if (agentTicketQuota.getRemainingQuantity() < buyerCount) {
            throw new BaseException(BaseExceptionEnum.INSUFFICIENT_STOCK);
        }

        Agent agent = agentRepository.findById(agentTicketQuota.getAgent().getId()).get();
        if (agent == null || agent.isDeleted() == true || agent.getStatus().equals(BaseStatus.INACTIVE)) {
            throw new BaseException(BaseExceptionEnum.CURRENT_USER_CAN_NOT_CREAT_ORDER);
        }

        // 扣除配额表的库存和库存表的库存
        int affectCounts = agentTicketQuotaRepository.addRemainingQuantity(agentTicketQuota.getId(), -buyerCount);
        if (affectCounts != 1) {
            throw new BaseException(BaseExceptionEnum.OPERATION_FAILED);
        }
        affectCounts = inventoryRepository.addRemainingQuantity(agentTicketQuota.getInventory().getId(), -buyerCount);
        if (affectCounts != 1) {
            throw new BaseException(BaseExceptionEnum.OPERATION_FAILED);
        }

        //创建订单信息
        BookingOrder order = new BookingOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setAgentTicketQuota(agentTicketQuota);
        order.setBuyCount(buyerCount);
        order.setAgent(agent);
        order.setBuyPrice(agentTicketQuota.getPrice());
        order.setConcertName(agentTicketQuota.getInventory().getConcert().getName());
        order.setSessionName(agentTicketQuota.getInventory().getSession().getName());
        order.setTicketCategoryName(agentTicketQuota.getInventory().getTicketCategory().getName());
        order.setTotalFee(agentTicketQuota.getPrice() * buyerCount);
        order.setStatus(OrderStatus.CREATED);

        List<BookingBuyer> buyers = new ArrayList<>();
        BookingOrder finalOrder = order;
        requestVO.getBuyerList().forEach(item -> {
            BookingBuyer orderbuyer = new BookingBuyer();
            orderbuyer.setBookingOrder(finalOrder);
            orderbuyer.setName(item.getName());
            orderbuyer.setIdNumber(item.getIdNumber());
            orderbuyer.setMobile(item.getMobile());
            buyers.add(orderbuyer);
        });
        order.setBuyerList(buyers);

        ArrayList<BookingOperation> operations = new ArrayList<>();
        BookingOperation operation = new BookingOperation();
        operation.setStatus(OrderStatus.CREATED);
        operation.setBookingOrder(order);
        operations.add(operation);
        order.setOperationList(operations);

        order = bookingOrderRepository.save(order);

        CreateOrderResponseDTO response = new CreateOrderResponseDTO();
        response.setOrderId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setTotalFee(order.getTotalFee());
        return response;
    }


    private static String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        Random random = new Random();
        int randomNum = random.nextInt(10000); // 生成一个0到9999的随机数
        return timestamp + String.format("%04d", randomNum); // 使用0进行补位，使随机数始终为4位
    }

    public List<HashMap<String, Object>> list(Long agentId) {
        List<BookingOrder> orders = bookingOrderRepository.findByAgentId(agentId);
        List<HashMap<String, Object>> convertedOrders = orders.stream().map(order -> convertBookingOrderToMap(order)).collect(Collectors.toList());
        return convertedOrders;
    }

    private static HashMap<String, Object> convertBookingOrderToMap(BookingOrder order) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("orderNumber", order.getOrderNumber());
        map.put("totalFee", order.getTotalFee());
        map.put("status", order.getStatus());
        map.put("statusDesc", order.getStatus().getName());
        map.put("buyCount", order.getBuyCount());
        map.put("createDate", order.getCreateDate().format(DATE_TIME_FORMATTER));
        map.put("concertName", order.getConcertName());
        map.put("sessionName", order.getSessionName());
        map.put("ticketCategoryName", order.getTicketCategoryName());
        List<Object> buyerList = order.getBuyerList().stream().map(bookingBuyer -> {
            HashMap<String, Object> bueryMap = new HashMap<>();
            bueryMap.put("id", bookingBuyer.getId());
            bueryMap.put("name", bookingBuyer.getName());
            bueryMap.put("idNumber", bookingBuyer.getIdNumber());
            bueryMap.put("mobile", bookingBuyer.getMobile());
            return bueryMap;
        }).collect(Collectors.toList());
        map.put("buyerList", buyerList);
        return map;
    }

    public HashMap<String, Object> orderDetail(Long orderId, Long agentId) throws BaseException {
        BookingOrder bookingOrder = bookingOrderRepository.findById(orderId).get();
        if (!bookingOrder.getAgent().getId().equals(agentId)) {
            throw new BaseException(BaseExceptionEnum.NOT_ALLOWED_OPERATION);
        }
        return convertBookingOrderToMap(bookingOrder);
    }
}
