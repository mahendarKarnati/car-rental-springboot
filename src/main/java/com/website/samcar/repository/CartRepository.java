package com.website.samcar.repository;

import com.website.samcar.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUsername(String username);
    CartItem findByUsernameAndProductId(String username, Long productId);
}
