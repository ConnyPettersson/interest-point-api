package com.example.interestpointapi.repositories;

import com.example.interestpointapi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findById(Integer id);
}
