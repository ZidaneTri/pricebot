package com.example.pricebot.service;


import com.example.pricebot.entity.Shop;

import java.util.Set;

public interface ShopService {

    Shop saveShop(Shop shop);

    Set<Shop> saveAll(Set<Shop> shops);

    boolean existsByCode(String code);

    Shop getByCode(String code);



}
