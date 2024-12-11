package com.example.interestpointapi.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import com.example.interestpointapi.dto.PlaceDTO;
import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.CategoryRepository;
import com.example.interestpointapi.repositories.UserRepository;
import com.example.interestpointapi.services.PlaceService;
import org.geolatte.geom.builder.DSL;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.geolatte.geom.Geometry;

import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/places")

public class PlaceController {
    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);
    private final PlaceService placeService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PlaceController(PlaceService placeService, UserRepository userRepository, CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.placeService = placeService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Integer id) {
        Place place = placeService.getPlaceById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place with ID " + id + " not found!"));
        return ResponseEntity.ok(place);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Place>> getPlacesByPrivacy(@RequestParam(required = false) Boolean isPrivate, Principal principal) {
        if (isPrivate == null) {
            throw new IllegalArgumentException("The 'isPrivate' parameter is required!");
        }

        if (isPrivate && principal == null) {
            throw new IllegalArgumentException("You must be logged in to access private places!");
        }

        List<Place> privatePlaces = placeService.getPlacesByPrivacy(isPrivate);
        return ResponseEntity.ok(privatePlaces);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Place>> getPublicPlacesByCategory(@PathVariable Integer id) {
       Category category = placeService.getCategoryById(id)
               .orElseThrow(() -> new IllegalArgumentException("Category with ID " + id + " not found!"));

       List<Place> publicPlaces = placeService.getPublicPlacesByCategory(category);
       return ResponseEntity.ok(publicPlaces);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity <List<Place>> getPlacesByUserId(@PathVariable Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found!"));
        List<Place> userPlaces = placeService.getPlacesByUserId(userId);
        return ResponseEntity.ok(userPlaces);
    }

    @GetMapping("/area")
    public ResponseEntity<List<Place>> getPlacesWithinArea(@RequestBody String geoJson) {
        try {
            Geometry<?> area = objectMapper.readValue(geoJson, Geometry.class);
            logger.debug("Received GeoJSON for area: {}", area);
            List<Place> places = placeService.getPlacesWithinArea(area);
            logger.debug("Places found within area: {}", places);
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid GeoJSON format: " + e.getMessage(), e);
        }
    }


    @GetMapping("/radius")
    public ResponseEntity<List<Place>> getPlacesWithinRadius(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        try {
            if (radius <= 0) {
                throw new IllegalArgumentException("Radius must be greater than 0");
            }
            List<Place> places = placeService.getPlacesWithinRadius(latitude, longitude, radius);
            return ResponseEntity.ok(places);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in radius query: " + e.getMessage(), e);
        }
    }





    @GetMapping("/my-places")
    public ResponseEntity<List<Place>> getMyPlaces(Principal principal) {
        String username = principal.getName();
        Integer userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " notfound!"))
                .getId();
        List<Place> myPlaces = placeService.getPlacesByUserId(userId);
        return ResponseEntity.ok(myPlaces);
    }



    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Place> createPlace(@RequestBody @Valid PlaceDTO placeDTO) {

        logger.debug("Coordinates received: {}", placeDTO.getCoordinates());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

               Integer userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found!"))
                .getId();

        Place place = new Place();
        place.setName(placeDTO.getName());
        place.setCategory(categoryRepository.findById(placeDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + placeDTO.getCategoryId() + " not found")));
        place.setUserId(userId);
        place.setPrivate(placeDTO.isPrivate());
        place.setDescription(placeDTO.getDescription());
        if (placeDTO.getCoordinates() != null) {
            place.setCoordinates(placeDTO.getCoordinates()); // Detta anropar setCoordinates(String wkt)
        }

        Place savedPlace = placeService.savePlace(place);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(
            @PathVariable Integer id,
            @RequestBody @Valid PlaceDTO placeDTO) {
        logger.debug("Coordinates received: {}", placeDTO.getCoordinates());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Integer userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found!"))
                .getId();

        logger.debug("Authenticated user ID: {}", userId);

        Place updatedPlace = new Place();
        updatedPlace.setName(placeDTO.getName());
        if (placeDTO.getCategoryId() != null) {
            updatedPlace.setCategory(categoryRepository.findById(placeDTO.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category with ID " + placeDTO.getCategoryId() + " not found")));
        }
        updatedPlace.setUserId(userId);
        updatedPlace.setPrivate(placeDTO.isPrivate());
        updatedPlace.setDescription(placeDTO.getDescription());
        if (placeDTO.getCoordinates() != null) {
            updatedPlace.setCoordinates(placeDTO.getCoordinates());
        }

        Place savedPlace = placeService.updatePlace(id, updatedPlace);
        return ResponseEntity.ok(savedPlace);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.noContent().build();
    }
}


