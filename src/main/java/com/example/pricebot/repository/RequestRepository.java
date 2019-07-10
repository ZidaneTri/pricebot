package com.example.pricebot.repository;

import com.example.pricebot.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request,Long> {

}
