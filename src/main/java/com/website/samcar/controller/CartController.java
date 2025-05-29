package com.website.samcar.controller;

import com.website.samcar.model.CartItem;
import com.website.samcar.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepo;

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam String productName,
                            @RequestParam double price,
                            Authentication auth) {
        String username = auth.getName();

        CartItem existingItem = cartRepo.findByUsernameAndProductId(username, productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartRepo.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setUsername(username);
            item.setProductId(productId);
            item.setProductName(productName);
            item.setPrice(price);
            item.setQuantity(1);
            cartRepo.save(item);
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Authentication auth) {
        List<CartItem> cartItems = cartRepo.findByUsername(auth.getName());
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam Long id) {
        cartRepo.deleteById(id);
        return "redirect:/cart";
    }
    @PostMapping("/cart/book")
    public String bookCartItems(Authentication auth, Model model) {
        String username = auth.getName();
        List<CartItem> cartItems = cartRepo.findByUsername(username);

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        // You can save cart items into a Booking table or just simulate booking
        cartRepo.deleteAll(cartItems); // Clear cart after booking

        model.addAttribute("success", "Items booked successfully!");
        return "redirect:/home"; // Or redirect to a booking confirmation page
    }

}
