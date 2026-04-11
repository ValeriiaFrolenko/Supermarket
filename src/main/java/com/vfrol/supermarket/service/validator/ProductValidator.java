package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;

@Singleton
public class ProductValidator extends BaseValidator{

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
        requireExists(categoryDAO.findById(dto.categoryId()), "Category with ID " + dto.categoryId() + " does not exist.");
    }

    public void validateForUpdate(ProductCreateDTO dto){
        requireExists(productDAO.findById(dto.id()), "Product with ID " + dto.id() + " does not exist.");
        requireExists(categoryDAO.findById(dto.categoryId()), "Category with ID " + dto.categoryId() + " does not exist.");
    }

    public void validateForDelete(int id){
        requireExists(productDAO.findById(id), "Product with ID " + id + " does not exist.");
        if (storeProductDAO.existsByProductId(id)) {
            throw new ValidationException("Cannot delete product with ID " + id + " because it is associated with store products.");
        }
    }
}