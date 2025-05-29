package com.website.samcar.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.website.samcar.model.Booking;
import com.website.samcar.model.Product;
import com.website.samcar.model.User;
import com.website.samcar.service.BookingService;
import com.website.samcar.service.CustomUserDetailsService;
import com.website.samcar.service.ProductService;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomUserDetailsService userService;

    @PostMapping("/book/{carId}")
    public String bookCar(@PathVariable Long carId,
                          @RequestParam("bookingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam("pickupLocation") String pickupLocation,
                          @RequestParam("returnDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
                          @RequestParam("nCars") long nCars,
                          Principal principal) {

        Product car = productService.getProductById(carId);
        User user = userService.findByUsername(principal.getName()); // ✅ Use username
        long days = ChronoUnit.DAYS.between(date, returnDate);
        Booking booking = new Booking();
        booking.setCar(car);
        booking.setUser(user);
        booking.setBookingDate(date);
        booking.setPickupLocation(pickupLocation);
        booking.setReturnDate(returnDate);
        booking.setDays(days);
        booking.setnCars(nCars);
        double totalPrice=days*car.getPrice()*booking.getnCars();
        booking.setTotalPrice(totalPrice);
        
        booking.setStatus("PENDING");

        bookingService.saveBooking(booking);

        return "redirect:/my-bookings";
    }

    @GetMapping("/my-bookings")
    public String viewMyBookings(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()); // ✅ Also use username here
        model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        model.addAttribute("user",user);
        return "my-bookings";
    }
    
    @GetMapping("/product-details/{id}")
    public String viewProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("today", LocalDate.now());
        return "product-details"; // Thymeleaf template name
    }
        
}

