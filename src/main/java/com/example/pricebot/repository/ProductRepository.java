package com.example.pricebot.repository;

import com.example.pricebot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product getByCode(String code);

    boolean existsByCode(String code);




}
