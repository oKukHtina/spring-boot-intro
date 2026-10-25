package com.example.controller;

import com.example.dto.request.OrderRequestDto;
import com.example.dto.request.StatusRequestDto;
import com.example.dto.response.OrderItemResponseDto;
import com.example.dto.response.OrderResponseDto;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create an order",
            description = "Create a new order"
    )
    public OrderResponseDto addOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto
    ) {
        return orderService.addOrder(orderRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(
            summary = "Get order history",
            description = "Get order history by current user"
    )
    public Page<OrderResponseDto> getOrderHistory(
            Pageable pageable
    ) {
        return orderService.getOrderHistory(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(
            summary = "Update order status",
            description = """
                Allows an administrator to update an order status.
                Allowed statuses: PENDING, DELIVERED, COMPLETED.
                """
    )
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusRequestDto statusRequestDto
    ) {
        return orderService.updateOrderStatus(id, statusRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}/items")
    @Operation(
            summary = "Get order items",
            description = "Get all order items by current user"
    )
    public Page<OrderItemResponseDto> getAllOrderItems(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return orderService.getAllOrderItems(id, pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    @Operation(
            summary = "Get order item",
            description = "Get order item by current user"
    )
    public OrderItemResponseDto getOrderItem(
            @PathVariable("orderId") Long orderId,
            @PathVariable("orderItemId") Long orderItemId
    ) {
        return orderService.getOrderItem(orderId, orderItemId);
    }
}
