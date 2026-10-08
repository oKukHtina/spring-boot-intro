package com.example.service;

import com.example.dto.request.CartItemRequestDto;
import com.example.dto.request.QuantityCartItemRequestDto;
import com.example.dto.response.CartItemResponseDto;
import com.example.dto.response.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart();

    CartItemResponseDto save(CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto update(
            Long cartItemId,
            QuantityCartItemRequestDto quantityCartItemRequestDto
    );

    void deleteById(Long id);

}
