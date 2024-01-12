package com.lab.dev.shawn.api.wx.order.service;

import com.lab.dev.shawn.api.base.constant.BaseExceptionEnum;
import com.lab.dev.shawn.api.base.constant.OrderStatus;
import com.lab.dev.shawn.api.base.exception.BaseException;
import com.lab.dev.shawn.api.entity.AgentTicketQuota;
import com.lab.dev.shawn.api.entity.Booking;
import com.lab.dev.shawn.api.entity.BookingBuyer;
import com.lab.dev.shawn.api.entity.BookingOperation;
import com.lab.dev.shawn.api.repository.AgentTicketQuotaRepository;
import com.lab.dev.shawn.api.repository.InventoryRepository;
import com.lab.dev.shawn.api.repository.OrderRepository;
import com.lab.dev.shawn.api.wx.order.dto.CreateOrderResponseDTO;
import com.lab.dev.shawn.api.wx.order.vo.CreateOrderRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(rollbackFor = Exception.class)
public class WxOrderService {
    @Autowired
    private AgentTicketQuotaRepository agentTicketQuotaRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    public CreateOrderResponseDTO create(CreateOrderRequestVO requestVO, Long agentId) throws BaseException {
        int buyerCount = requestVO.getBuyers().size();
        //查询配额
        AgentTicketQuota agentTicketQuota = agentTicketQuotaRepository.findById(requestVO.getQuotaId()).get();
        if (agentTicketQuota.getRemainingQuantity() < buyerCount) {
            throw new BaseException(BaseExceptionEnum.INSUFFICIENT_STOCK);
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
        Booking order = new Booking();
        order.setOrderNumber(generateOrderNumber());
        order.setAgentTicketQuota(agentTicketQuota);
        order.setBuyCount(buyerCount);
        order.setTotalFee(agentTicketQuota.getPrice() * buyerCount);
        order.setStatus(OrderStatus.CREATED);

        List<BookingBuyer> buyers = new ArrayList<>();
        requestVO.getBuyers().forEach(item -> {
            BookingBuyer orderbuyer = new BookingBuyer();
            orderbuyer.setName(item.getName());
            orderbuyer.setIdNumber(item.getIdNumber());
            orderbuyer.setMobile(item.getMobile());
            buyers.add(orderbuyer);
        });
        order.setBuyers(buyers);

        ArrayList<BookingOperation> operations = new ArrayList<>();
        BookingOperation operation = new BookingOperation();
        operation.setStatus(OrderStatus.CREATED);
        operations.add(operation);
        order.setOperations(operations);

        order = orderRepository.save(order);

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
}
