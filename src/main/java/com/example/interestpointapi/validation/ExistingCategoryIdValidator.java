package com.example.interestpointapi.validation;

import com.example.interestpointapi.repositories.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingCategoryIdValidator implements ConstraintValidator<ExistingCategoryId, Integer> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean isValid(Integer categoryId, ConstraintValidatorContext context) {
        if (categoryId == null) {
            return true;
        }
        return categoryRepository.existsById(categoryId);
    }
}
