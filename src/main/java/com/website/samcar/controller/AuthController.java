package com.website.samcar.controller;

import com.website.samcar.model.Product;
import com.website.samcar.model.User;
import com.website.samcar.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String country, @RequestParam long mobile,@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCountry(country);
        user.setMobile(mobile);
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole("ROLE_USER");
        userRepo.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String verifyUser(@RequestParam String username,
                             @RequestParam long mobile,
                             Model model) {
    	Optional<User> userOptional = userRepo.findByUsernameAndMobile(username, mobile);
    	if (userOptional.isPresent()) {
    	    model.addAttribute("username", username);
    	    return "reset_password";
    	} else {
    	    model.addAttribute("error", "User not found. Please check your details.");
    	    return "forgot_password";
    	}

    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            model.addAttribute("username", username);
            return "reset_password";
        }

        Optional<User> userOptional = userRepo.findByUsername(username);
        model.addAttribute("status","deactive");
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(encoder.encode(password));
            userRepo.save(user);
            model.addAttribute("success", "Password updated successfully.");
            model.addAttribute("status","active");
        } else {
            model.addAttribute("error", "User not found.");
            model.addAttribute("status","deactive");
        }


        return "reset_password";
    }
    
    
    
//    @GetMapping("/edit-profile/{id}")
//   @PreAuthorize("hasRole('ADMIN')")
//    public String showUpdateProfileForm(@PathVariable Long id, Model model) {
//        Optional<User> user = userRepo.getUserById(id);
//        if (user == null) {
//            return "redirect:/error"; // or custom 404 page
//        }
//        model.addAttribute("user", user);
//        return "edit_userInfo"; // Same form as add_product but reused
//    }
    
    @GetMapping("/edit-profile/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        Optional<User> user = userRepo.findById(id);
        model.addAttribute("user", user);
        return "edit_userInfo";
    }

    
    

    @PostMapping("/update-profile")
    		public String updateProduct(@RequestParam long id,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String country, @RequestParam long mobile,@RequestParam String username, @RequestParam String password) {
    	        User user = new User();
    	        user.setId(id);
    	        user.setFirstName(firstName);
    	        user.setLastName(lastName);
    	        user.setCountry(country);
    	        user.setMobile(mobile);
    	        user.setUsername(username);
    	        user.setPassword(encoder.encode(password));
    	        user.setRole("ROLE_USER");
    	        userRepo.save(user);
    	        return "redirect:/my-bookings";
    	    }
    
    

}
