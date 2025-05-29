package com.website.samcar.repository;

import java.util.List;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.website.samcar.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findFirstByUsername(String username);

    Optional<User> getUserById(long id);
    Optional<User> findByUsernameAndMobile(String username, long mobile);  // <-- FIXED
    boolean existsByUsername(String username);
  List<User> findByRole(String role);
}
