package com.example.controller;

import com.example.dto.request.CartItemRequestDto;
import com.example.dto.request.QuantityCartItemRequestDto;
import com.example.dto.response.CartItemResponseDto;
import com.example.dto.response.ShoppingCartResponseDto;
import com.example.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(
            summary = "Create cart item",
            description = "Add book to the shopping cart"
    )
    @ResponseStatus(HttpStatus.CREATED)
    CartItemResponseDto createCartItem(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto
    ) {
        return shoppingCartService.save(cartItemRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(
            summary = "Get shopping cart",
            description = "Retrieve user's shopping cart"
    )
    ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getCart();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{id}")
    @Operation(
            summary = "Update quantity of a book",
            description = "Update quantity of a book in the shopping cart"
    )
    CartItemResponseDto updateCartItem(
            @PathVariable Long id,
            @RequestBody @Valid QuantityCartItemRequestDto quantityCartItemRequestDto
    ) {
        return shoppingCartService.update(id, quantityCartItemRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{id}")
    @Operation(
            summary = "Delete cart item",
            description = "Remove a book from the shopping cart"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCartItem(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
    }
}
