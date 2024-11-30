package com.example.interestpointapi.services;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.CategoryRepository;
import com.example.interestpointapi.repositories.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public Place updatePlace(Integer id, Place updatedPlace) {
        Place existingPlace = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not foung with ID: " + id));

        if (updatedPlace.getName() != null) {
            existingPlace.setName(updatedPlace.getName());
        }
        if (updatedPlace.getDescription() != null) {
            existingPlace.setDescription(updatedPlace.getDescription());
        }
        if (updatedPlace.getCoordinates() != null) {
            existingPlace.setCoordinates(updatedPlace.getCoordinates());
        }
        if (updatedPlace.getCategory() != null && updatedPlace.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedPlace.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            existingPlace.setCategory(category);
        }
        existingPlace.setPrivate(updatedPlace.isPrivate());

        return placeRepository.save(existingPlace);
    }

    public void deletePlace(Integer id) {
        if (!placeRepository.existsById(id)) {
            throw new IllegalArgumentException("Place not found with ID: " + id);
        }
        placeRepository.deleteById(id);
    }
}
