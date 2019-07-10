package com.example.pricebot.repository;

import com.example.pricebot.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop getByCode(String code);

    boolean existsByCode(String code);

}
