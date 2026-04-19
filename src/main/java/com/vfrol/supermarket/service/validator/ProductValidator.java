package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;

@Singleton
public class ProductValidator {

    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final StoreProductDAO storeProductDAO;

    @Inject
    public ProductValidator(ProductDAO productDAO, CategoryDAO categoryDAO, StoreProductDAO storeProductDAO) { // Змінено тут
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.storeProductDAO = storeProductDAO;
    }

    public void validateForCreate(ProductCreateDTO dto){
        if (!categoryDAO.existsById(dto.categoryId())) {
            throw new ValidationException("Category with ID " + dto.categoryId() + " does not exist.");
        }
    }

    public void validateForUpdate(ProductCreateDTO dto){
        if (!productDAO.existsById(dto.id())) {
            throw new ValidationException("Product with ID " + dto.id() + " does not exist.");
        }
        if (!categoryDAO.existsById(dto.categoryId())) {
            throw new ValidationException("Category with ID " + dto.categoryId() + " does not exist.");
        }
    }

    public void validateForDelete(int id){
        if (!productDAO.existsById(id)) {
            throw new ValidationException("Product with ID " + id + " does not exist.");
        }
        if (storeProductDAO.existsByProductId(id)) {
            throw new ValidationException("Cannot delete product with ID " + id + " because it is associated with store products.");
        }
    }
}