package com.example.service.impl;

import com.example.dto.response.OrderItemResponseDto;
import com.example.entity.OrderItem;
import com.example.entity.User;
import com.example.mapper.OrderItemMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Page<OrderItemResponseDto> getAllOrderItems(Long orderId, Pageable pageable) {
        User currentUser = getCurrentUser();
        checkOrderOwnership(orderId, currentUser.getId());

        return orderItemRepository.findAllByOrderId(orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId) {
        User currentUser = getCurrentUser();
        checkOrderOwnership(orderId, currentUser.getId());

        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(orderItemId, orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order item not found")
                );
        return orderItemMapper.toDto(orderItem);
    }

    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        return (User) authentication.getPrincipal();
    }

    private void checkOrderOwnership(Long orderId, Long userId) {
        orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order not found for the current user")
                );

    }
}
