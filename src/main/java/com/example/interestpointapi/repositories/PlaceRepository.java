package com.example.interestpointapi.repositories;

import com.example.interestpointapi.entities.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
