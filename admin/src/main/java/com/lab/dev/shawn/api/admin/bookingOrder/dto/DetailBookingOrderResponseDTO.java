package com.lab.dev.shawn.api.admin.bookingOrder.dto;

import com.lab.dev.shawn.api.entity.BookingBuyer;
import lombok.Data;

import java.util.List;

@Data
public class DetailBookingOrderResponseDTO {
    private List<BookingBuyer> buyerList;

}
