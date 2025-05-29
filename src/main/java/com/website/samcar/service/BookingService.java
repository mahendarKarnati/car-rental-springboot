package com.website.samcar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.website.samcar.model.Booking;
import com.website.samcar.model.User;
import com.website.samcar.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public void saveBooking(Booking booking) {
         bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

}

