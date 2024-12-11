package com.example.interestpointapi.services;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.CategoryRepository;
import com.example.interestpointapi.repositories.PlaceRepository;
import org.geolatte.geom.*;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class PlaceService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
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

        if (place.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        if (place.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates must be a valid WKT string");
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

    public List<Place> getPlacesWithinArea(Geometry<?> area) {
        logger.debug("Received area for query: {}", area);
        if (area == null) {
            logger.error("Area is null!");
            throw new IllegalArgumentException("Area cannot be null");
        }
        if (area.getSRID() != 4326) {
            logger.error("Invalid SRID: {}", area.getSRID());
            throw new IllegalArgumentException("Area SRID must be 4326");
        }
        String wktPolygon = area.toString();
// Ta bort "SRID=4326;" om den finns
        if (wktPolygon.startsWith("SRID=4326;")) {
            wktPolygon = wktPolygon.substring("SRID=4326;".length());
        }

// Nu anropar du native query
        List<Place> places = placeRepository.findByCoordinatesWithinNative(wktPolygon);

        logger.debug("Found places within area: {}", places);
        return places;
    }
}
