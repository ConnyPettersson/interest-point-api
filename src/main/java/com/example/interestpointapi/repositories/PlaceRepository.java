package com.example.interestpointapi.repositories;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    List<Place> findByIsPrivate(Boolean isPrivate);
    List<Place> findByIsPrivateFalseAndCategory(Category category);
}
