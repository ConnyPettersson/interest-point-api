package com.example.interestpointapi.services;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.CategoryRepository;
import com.example.interestpointapi.repositories.PlaceRepository;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;

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


    public Place updatePlace(Integer id, Place updatedPlace) {
        Place existingPlace = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found with ID: " + id));

        if (updatedPlace.getName() != null) {
            existingPlace.setName(updatedPlace.getName());
        }

        if (updatedPlace.getDescription() != null) {
            existingPlace.setDescription(updatedPlace.getDescription());
        }

        if (updatedPlace.getCoordinates() != null) {
            Object coordinates = updatedPlace.getCoordinates();
            if (coordinates instanceof String) {
                existingPlace.setCoordinates((String) coordinates);
            } else if (coordinates instanceof Point) {
                existingPlace.setCoordinates(coordinates.toString());
            } else {
                throw new IllegalArgumentException("Invalid coordinates format");
            }
        }

        if (updatedPlace.getCategory() != null) {
            existingPlace.setCategory(updatedPlace.getCategory());
        }

        if (updatedPlace.getUserId() != null) {
            existingPlace.setUserId(updatedPlace.getUserId());
        }
        return placeRepository.save(existingPlace);
    }




    public void deletePlaceById(Integer id) {
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Place with ID " + id + " not found!");
        }
    }

    public List<Place> getPlacesByPrivacy(Boolean isPrivate) {
        return placeRepository.findByIsPrivate(isPrivate);
    }

    public List<Place> getPublicPlacesByCategory(Category category) {
        return placeRepository.findByIsPrivateFalseAndCategory(category);
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public List<Place> getPlacesByUserId(Integer userId) {
        return placeRepository.findByUserId(userId);
    }

    public List<Place> getPlacesWithinArea(Geometry area) {
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null");
        }
        return placeRepository.findByCoordinatesWithin(area);
    }

    public List<Place> getPlacesWithinRadius(double latitude, double longitude, double radius) {

        var crs = CoordinateReferenceSystems.WGS84;

        Point<G2D> center = DSL.point(crs, DSL.g(longitude, latitude));

        return placeRepository.findByCoordinatesWithinRadius(center, radius);
    }


}
