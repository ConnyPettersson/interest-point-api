package com.example.interestpointapi.services;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.CategoryRepository;
import com.example.interestpointapi.repositories.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    public PlaceService(PlaceRepository placeRepository, CategoryRepository categoryRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
    }

    public Place savePlace(Place place) {

        if (place.getCategory() != null && place.getCategory().getId() != null) {
            Category category = categoryRepository.findById(place.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            place.setCategory(category);
        }

        Object coordinates = place.getCoordinates();
        if (coordinates == null) {

            throw new IllegalArgumentException("Coordinates must be either a valid WKT String or Geometry.");
        }

        return placeRepository.save(place);
    }


    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> getPlaceById(Integer id) {
        return placeRepository.findById(id);
    }

    public void deletePlaceById(Integer id) {
        placeRepository.deleteById(id);
    }
}
