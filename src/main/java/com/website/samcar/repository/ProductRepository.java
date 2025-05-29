package com.website.samcar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.website.samcar.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
