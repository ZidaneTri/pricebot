package com.example.pricebot.service.impl;

import com.example.pricebot.entity.Request;
import com.example.pricebot.repository.RequestRepository;
import com.example.pricebot.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {

    final
    RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    @Override
    public Request saveRequest(Request request) {
        return requestRepository.saveAndFlush(request);
    }

    @Override
    public void delete(Request request) {
        requestRepository.delete(request);
    }
}
