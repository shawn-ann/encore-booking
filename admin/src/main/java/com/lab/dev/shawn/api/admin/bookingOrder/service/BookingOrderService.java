package com.lab.dev.shawn.api.admin.bookingOrder.service;

import com.lab.dev.shawn.api.admin.bookingOrder.dto.DetailBookingOrderResponseDTO;
import com.lab.dev.shawn.api.admin.bookingOrder.vo.SearchBookingOrderRequestVO;
import com.lab.dev.shawn.api.entity.BookingOrder;
import com.lab.dev.shawn.api.repository.BookingOrderRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class BookingOrderService {
    @Autowired
    private BookingOrderRepository bookingOrderRepository;


    public Page<BookingOrder> list(SearchBookingOrderRequestVO requestVO) {
        Pageable pageable = PageRequest.of(requestVO.getPage() - 1, requestVO.getLimit());
        String beginDate = null;
        if (StringUtils.isNotBlank(requestVO.getDateRange()[0])) {
            beginDate = requestVO.getDateRange()[0];
        }

        String endDate = null;
        if (StringUtils.isNotBlank(requestVO.getDateRange()[1])) {
            endDate = requestVO.getDateRange()[1];
        }
        return bookingOrderRepository.findByConcertNameAndAgentMobile(requestVO.getConcertName(), requestVO.getAgentMobile(), beginDate, endDate, pageable);
    }

    public DetailBookingOrderResponseDTO detail(Long id) {
        BookingOrder order = bookingOrderRepository.findByID(id);

        DetailBookingOrderResponseDTO result = new DetailBookingOrderResponseDTO();
        result.setBuyerList(order.getBuyerList());

        return result;
    }
}
