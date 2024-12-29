package com.example.quanlykho.dao;

import com.example.quanlykho.entity.Product;

import java.util.List;

public class ProductDao {
    private final GenericDao<Product, String> genericDao;

    public ProductDao() {
        genericDao = new GenericDao<>(Product.class);
    }

    public List<Product> getAllProducts() {
        return genericDao.findAll();
    }

    public boolean isExistedProduct(String id) {
        return genericDao.findById(id) != null;
    }

    public void saveProduct(Product product) {
        genericDao.save(product);
    }

    public void updateProduct(Product product) {
        genericDao.update(product);
    }

    public void deleteProduct(Product product) {
        genericDao.delete(product);
    }
}
