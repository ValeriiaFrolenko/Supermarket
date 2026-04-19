package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;

@Singleton
public class CategoryValidator {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    @Inject
    public CategoryValidator(CategoryDAO categoryDAO, ProductDAO productDAO) { // Змінено тут
        this.categoryDAO = categoryDAO;
        this.productDAO = productDAO;
    }

    public void validateForUpdate(CategoryCreateDTO dto) {
       if (!categoryDAO.existsById(dto.id()))
           throw new ValidationException("Category with ID '" + dto.id() + "' does not exist.");
    }

    public void validateForDelete(int id) {
        if (!categoryDAO.existsById(id))
            throw new ValidationException("Category with ID '" + id + "' does not exist.");

        if (productDAO.existsByCategoryId(id)) {
            throw new ValidationException(
                    "Cannot delete category with associated products. Reassign or delete those products first."
            );
        }
    }
}