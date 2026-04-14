package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.entity.Category;
import com.vfrol.supermarket.service.validator.CategoryValidator;
import com.vfrol.supermarket.service.validator.ValidationException;

import java.util.List;

@Singleton
public class CategoryService {
    private final CategoryDAO categoryDAO;
    private final CategoryValidator categoryValidator;

    @Inject
    public CategoryService(CategoryDAO categoryDAO, CategoryValidator validator) {
        this.categoryDAO = categoryDAO;
        this.categoryValidator = validator;
    }

    public void addCategory(CategoryCreateDTO categoryCreateDTO){
        Category category = new Category(0, categoryCreateDTO.name());
        categoryDAO.create(category);
    }

    public void updateCategory(CategoryCreateDTO categoryCreateDTO){
        categoryValidator.validateForUpdate(categoryCreateDTO);
        Category category = new Category(categoryCreateDTO.id(), categoryCreateDTO.name());
        categoryDAO.update(category);
    }

    public void deleteCategory(int id){
        categoryValidator.validateForDelete(id);
        categoryDAO.delete(id);
    }

    public CategoryListDTO getCategoryById(int id){
        return categoryDAO.findById(id).orElseThrow(() -> new ValidationException("Category not found"));
    }

    public List<CategoryListDTO> getAllCategories(){
        return categoryDAO.findAll();
    }

    public List<CategoryListDTO> getCategoriesByName(String name) {
        return categoryDAO.findByName(name);
    }
}
