package com.example.pricebot.service;

import com.example.pricebot.entity.Product;

public interface ProductService {

    Product saveProduct (Product product);

    Product getByCode(String code);

    boolean existsByCode(String code);

}
