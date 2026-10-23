package com.example.service;

import com.example.dto.request.OrderRequestDto;
import com.example.dto.request.StatusRequestDto;
import com.example.dto.response.OrderItemResponseDto;
import com.example.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponseDto> getOrderHistory(Pageable pageable);

    OrderResponseDto addOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto updateOrderStatus(Long id, StatusRequestDto statusRequestDto);

    Page<OrderItemResponseDto> getAllOrderItems(Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId);
}
