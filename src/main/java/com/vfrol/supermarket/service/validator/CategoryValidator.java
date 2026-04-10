package com.vfrol.supermarket.service.validator;

import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;
import com.vfrol.supermarket.service.ProductService;

public class CategoryValidator extends BaseValidator {

    private final CategoryDAO categoryDAO;
    private final ProductService productService;

    public CategoryValidator(CategoryDAO categoryDAO, ProductService productService) {
        this.categoryDAO = categoryDAO;
        this.productService = productService;
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

        if (categoryDAO.count() <= 1) {
            throw new ValidationException(
                    "Cannot delete the last category. Create another category first."
            );
        }

        if (productService.categoryExists(id)) {
            throw new ValidationException(
                    "Cannot delete category with associated products. Reassign or delete those products first."
            );
        }
    }
}
