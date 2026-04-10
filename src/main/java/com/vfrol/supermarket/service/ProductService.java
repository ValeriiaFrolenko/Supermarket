package com.vfrol.supermarket.service;

import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.dto.product.ProductListDTO;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.entity.Product;
import com.vfrol.supermarket.filter.ProductFilter;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void addProduct(ProductCreateDTO dto) {
        Product product = Product.builder()
                .categoryId(dto.categoryId())
                .name(dto.name())
                .characteristics(dto.characteristics())
                .build();
        productDAO.create(product);
    }

    public void updateProduct(ProductCreateDTO dto) {
        Product product = Product.builder()
                .id(dto.id())
                .categoryId(dto.categoryId())
                .name(dto.name())
                .characteristics(dto.characteristics())
                .build();
        productDAO.update(product);
    }

    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    public ProductDetailsDTO getProductById(int id) {
        return productDAO.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<ProductNameDTO> getProductByName(String name) {
        return productDAO.findByName(name);
    }

    public List<ProductListDTO> getAllProducts() {
        return productDAO.findAll();
    }

    public List<ProductNameDTO> getAllProductNames() {
        return productDAO.findAllNames();
    }

    public List<ProductListDTO> getProductsByFilter(ProductFilter filter) {
        return productDAO.findByFilter(filter);
    }

    public boolean categoryExists(int id){
        return productDAO.categoryExists(id);
    }
}