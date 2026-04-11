package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;

@Singleton
public class CategoryValidator extends BaseValidator {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    @Inject
    public CategoryValidator(CategoryDAO categoryDAO, ProductDAO productDAO) { // Змінено тут
        this.categoryDAO = categoryDAO;
        this.productDAO = productDAO;
    }

    public void validateForUpdate(CategoryCreateDTO dto) {
        requireExists(
                categoryDAO.findById(dto.id()),
                "Category with ID '" + dto.id() + "' does not exist."
        );
    }

    public void validateForDelete(int id) {
        requireExists(
                categoryDAO.findById(id),
                "Category with ID '" + id + "' does not exist."
        );

        if (productDAO.existsByCategoryId(id)) {
            throw new ValidationException(
                    "Cannot delete category with associated products. Reassign or delete those products first."
            );
        }
    }
}