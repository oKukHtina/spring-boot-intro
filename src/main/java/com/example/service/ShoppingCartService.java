package com.example.service;

import com.example.dto.request.CartItemRequestDto;
import com.example.dto.request.QuantityCartItemRequestDto;
import com.example.dto.response.ShoppingCartResponseDto;
import com.example.entity.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart();

    ShoppingCartResponseDto save(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto update(
            Long cartItemId,
            QuantityCartItemRequestDto quantityCartItemRequestDto
    );

    void deleteById(Long id);

    void createShoppingCart(User user);

}
