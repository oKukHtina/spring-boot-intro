package com.example.repository;

import com.example.entity.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(
            Long shoppingCartId,
            Long bookId
    );

    Optional<CartItem> findByIdAndShoppingCartId(
            Long cartItemId,
            Long shoppingCartId
    );

    boolean existsByIdAndShoppingCartId(Long cartItemId, Long shoppingCartId);
}
