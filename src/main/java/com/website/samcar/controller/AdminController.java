package com.website.samcar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.website.samcar.model.Booking;
import com.website.samcar.model.User;
import com.website.samcar.repository.UserRepository;
import com.website.samcar.service.BookingService;
import com.website.samcar.service.ProductService;

@Controller
public class AdminController {
	@Autowired
    private BookingService bookingService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private ProductService productService;
	
	@GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String getProductsList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "dashboard";
    }
	
	
	@GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAllBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin-bookings"; // This should match your Thymeleaf template name
    }
	
	@PostMapping("/bookings/{id}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public String acceptBookingRequest(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null && booking.getStatus().equals("PENDING")) {
            booking.setStatus("ACCEPTED");
            bookingService.saveBooking(booking);
        }
        return "redirect:/bookings";
    }


    @PostMapping("/bookings/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null && booking.getStatus().equals("PENDING")) {
            booking.setStatus("REJECTED");
            bookingService.saveBooking(booking);
        }
        return "redirect:/bookings";
    }

}

