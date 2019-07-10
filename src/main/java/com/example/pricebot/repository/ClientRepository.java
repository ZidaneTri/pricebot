package com.example.pricebot.repository;

import com.example.pricebot.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {

    boolean existsByChatId(Long chatId);

    Client getByChatId(Long chatId);

    boolean existsByChatIdAndAddStateGreaterThan(String chatId, int addState);
}
