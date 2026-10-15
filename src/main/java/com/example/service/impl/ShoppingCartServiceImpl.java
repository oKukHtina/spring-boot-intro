package com.example.service.impl;

import com.example.dto.request.CartItemRequestDto;
import com.example.dto.request.QuantityCartItemRequestDto;
import com.example.dto.response.ShoppingCartResponseDto;
import com.example.entity.Book;
import com.example.entity.CartItem;
import com.example.entity.ShoppingCart;
import com.example.entity.User;
import com.example.mapper.ShoppingCartMapper;
import com.example.repository.BookRepository;
import com.example.repository.CartItemRepository;
import com.example.repository.ShoppingCartRepository;
import com.example.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper mapper;

    @Override
    public ShoppingCartResponseDto getCart() {
        ShoppingCart shoppingCart = getCurrentUserCart();

        return mapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto save(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getCurrentUserCart();

        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Book was not found with id : " + cartItemRequestDto.bookId()
                        )
                );

        CartItem cartItem = cartItemRepository.findByShoppingCartIdAndBookId(
                shoppingCart.getId(),
                book.getId())
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setShoppingCart(shoppingCart);
                    newCartItem.setBook(book);
                    shoppingCart.getCartItems().add(newCartItem);
                    return newCartItem;
                });

        cartItem.setQuantity(
                cartItem.getQuantity() + cartItemRequestDto.quantity()
        );

        cartItemRepository.save(cartItem);

        return mapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto update(
            Long cartItemId,
            QuantityCartItemRequestDto quantityCartItemRequestDto
    ) {
        ShoppingCart shoppingCart = getCurrentUserCart();

        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Cart item was not found in the current user's cart"
                        )
                );

        cartItem.setQuantity(quantityCartItemRequestDto.quantity());
        cartItemRepository.save(cartItem);

        return mapper.toDto(shoppingCart);
    }

    @Override
    public void deleteById(Long id) {
        ShoppingCart shoppingCart = getCurrentUserCart();

        if (!cartItemRepository.existsByIdAndShoppingCartId(
                id,
                shoppingCart.getId()
        )) {
            throw new EntityNotFoundException(
                    "Cart item was not found in the current user's cart"
            );
        }

        cartItemRepository.deleteById(id);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        shoppingCartRepository.save(shoppingCart);
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
                                "Shopping cart was not found"
                        )
                );
    }
}
