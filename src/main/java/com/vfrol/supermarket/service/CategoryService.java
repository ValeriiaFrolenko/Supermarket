package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CategoryDAO;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.entity.Category;
import com.vfrol.supermarket.exception.ValidationException;

import java.util.List;

@Singleton
public class CategoryService {
    private final CategoryDAO categoryDAO;

    @Inject
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public void addCategory(CategoryCreateDTO categoryCreateDTO) {
        categoryDAO.create(new Category(0, categoryCreateDTO.name()));
    }

    public void updateCategory(CategoryCreateDTO categoryCreateDTO) {
        categoryDAO.update(new Category(categoryCreateDTO.id(), categoryCreateDTO.name()));
    }

    public void deleteCategory(int id) {
        categoryDAO.delete(id);
    }

    public CategoryListDTO getCategoryById(int id) {
        return categoryDAO.findById(id).orElseThrow(() -> new ValidationException("Category not found"));
    }

    public List<CategoryListDTO> getAllCategories() {
        return categoryDAO.findAll();
    }

    public List<CategoryListDTO> getCategoriesByName(String name) {
        return categoryDAO.findByName(name);
    }
}