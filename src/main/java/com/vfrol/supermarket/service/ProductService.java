package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.dto.product.ProductListDTO;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.entity.Product;
import com.vfrol.supermarket.filter.ProductFilter;

import java.util.List;

@Singleton
public class ProductService {

    private final ProductDAO productDAO;

    @Inject
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void addProduct(ProductCreateDTO dto) {
        productDAO.create(Product.builder()
                .categoryId(dto.categoryId())
                .name(dto.name())
                .manufacturer(dto.manufacturer())
                .characteristics(dto.characteristics())
                .build());
    }

    public void updateProduct(ProductCreateDTO dto) {
        productDAO.update(Product.builder()
                .id(dto.id())
                .categoryId(dto.categoryId())
                .name(dto.name())
                .manufacturer(dto.manufacturer())
                .characteristics(dto.characteristics())
                .build());
    }

    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    public ProductDetailsDTO getProductById(int id) {
        return productDAO.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<ProductListDTO> getAllProducts() {
        return productDAO.findAll();
    }

    public List<ProductNameDTO> getAllProductNames() {
        return productDAO.findAllNames();
    }

    public List<ProductDetailsDTO> getAllProductDetails() {
        return productDAO.findAllDetails();
    }

    public List<ProductListDTO> getProductsByFilter(ProductFilter filter) {
        return productDAO.findByFilter(filter);
    }
}