package com.example.interestpointapi.services;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository  categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }
}
