package com.example.pricebot.service.impl;


import com.example.pricebot.entity.Client;
import com.example.pricebot.entity.Request;
import com.example.pricebot.repository.ClientRepository;
import com.example.pricebot.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    final
    ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Client getByChatId(Long chatId){
        return clientRepository.getByChatId(chatId);
    }

    @Override
    public boolean existsByChatId(Long chatId){
        return clientRepository.existsByChatId(chatId);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.saveAndFlush(client);
    }

    @Override
    public boolean existsByChatIdAndAddStateGreaterThan(String chatId, int addState) {
        return clientRepository.existsByChatIdAndAddStateGreaterThan(chatId,addState);
    }


}
