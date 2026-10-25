package com.example.service.impl;

import com.example.dto.request.OrderRequestDto;
import com.example.dto.request.StatusRequestDto;
import com.example.dto.response.OrderItemResponseDto;
import com.example.dto.response.OrderResponseDto;
import com.example.entity.CartItem;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.entity.ShoppingCart;
import com.example.entity.User;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.ShoppingCartRepository;
import com.example.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Page<OrderResponseDto> getOrderHistory(Pageable pageable) {
        User currentUser = getCurrentUser();

        return orderRepository.findAllByUserId(currentUser.getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
        ShoppingCart currentUserCart = getCurrentUserCart();

        if (currentUserCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart");
        }

        Order order = new Order();
        order.setUser(currentUserCart.getUser());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);

        Set<OrderItem> orderItems = currentUserCart.getCartItems()
                .stream()
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toSet());

        BigDecimal total = calculateTotal(orderItems);
        order.setTotal(total);

        orderRepository.save(order);
        currentUserCart.getCartItems().clear();

        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, StatusRequestDto statusRequestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order was not found with id:" + id)
        );

        order.setStatus(statusRequestDto.status());
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

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

    private ShoppingCart getCurrentUserCart() {
        User currentUser = getCurrentUser();

        return shoppingCartRepository.findByUserId(currentUser.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Shopping cart was not found with id: "
                                        + currentUser.getId()
                        )
                );
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void checkOrderOwnership(Long orderId, Long userId) {
        orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Order not found for the current user"
                        )
                );

    }
}
