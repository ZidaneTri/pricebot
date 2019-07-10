package com.example.pricebot.service;

import com.example.pricebot.entity.Request;

public interface RequestService {

    Request saveRequest (Request request);

    void delete(Request request);

}
