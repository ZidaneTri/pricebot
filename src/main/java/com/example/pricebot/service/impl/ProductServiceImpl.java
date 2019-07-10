package com.example.pricebot.service.impl;

import com.example.pricebot.entity.Product;
import com.example.pricebot.repository.ProductRepository;
import com.example.pricebot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    final
    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public Product saveProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    public Product getByCode(String code) {
        return productRepository.getByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }
}
