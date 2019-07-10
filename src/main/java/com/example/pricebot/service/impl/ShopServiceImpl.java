package com.example.pricebot.service.impl;

import com.example.pricebot.entity.Shop;
import com.example.pricebot.repository.ShopRepository;
import com.example.pricebot.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ShopServiceImpl implements ShopService {

    final
    ShopRepository shopRepository;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Shop saveShop(Shop shop) {
        return shopRepository.saveAndFlush(shop);
    }

    @Override
    public Set<Shop> saveAll(Set<Shop> shops) {
      return new HashSet<Shop>(shopRepository.saveAll(shops));
    }

    @Override
    public boolean existsByCode(String code) {
        return shopRepository.existsByCode(code);
    }

    @Override
    public Shop getByCode(String code) {
        return shopRepository.getByCode(code);
    }
}
