package com.example.pricebot.service;

import com.example.pricebot.entity.Client;
import com.example.pricebot.entity.Request;


import java.util.Set;

public interface ClientService {

    Client getByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    Client saveClient(Client client);

    boolean existsByChatIdAndAddStateGreaterThan(String chatId, int addState);
}
