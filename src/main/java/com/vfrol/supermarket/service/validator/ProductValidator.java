package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;
import com.vfrol.supermarket.service.CategoryService;
import com.vfrol.supermarket.service.StoreProductService;

@Singleton
public class ProductValidator extends BaseValidator{

    private final ProductDAO productDAO;
    private final CategoryService categoryService;
    private final StoreProductService storeProductService;

    @Inject
    public ProductValidator(ProductDAO productDAO, CategoryService categoryService, StoreProductService storeProductService) {
        this.productDAO = productDAO;
        this.categoryService = categoryService;
        this.storeProductService = storeProductService;
    }

    public void validateForCreate(ProductCreateDTO dto){
        categoryService.getCategoryById(dto.categoryId());
    }

    public void validateForUpdate(ProductCreateDTO dto){
        requireExists(productDAO.findById(dto.id()), "Product with ID " + dto.id() + " does not exist.");
        if (categoryService.getCategoryById(dto.categoryId()) == null) {
            throw new ValidationException("Category with ID " + dto.categoryId() + " does not exist.");
        }
    }

    public void validateForDelete(int id){
        requireExists(productDAO.findById(id), "Product with ID " + id + " does not exist.");
        if (storeProductService.existsByProductId(id)) {
            throw new ValidationException("Cannot delete product with ID " + id + " because it is associated with store products.");
        }
    }
}
