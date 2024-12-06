package com.example.interestpointapi.controllers;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.repositories.UserRepository;
import com.example.interestpointapi.services.PlaceService;
import org.geolatte.geom.Geometry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/places")

public class PlaceController {
    private final PlaceService placeService;
    private final UserRepository userRepository;

    public PlaceController(PlaceService placeService, UserRepository userRepository) {
        this.placeService = placeService;
        this.userRepository = userRepository;
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
    public ResponseEntity <List<Place>> getPlacesWithinAreas(@RequestBody Geometry area) {
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null!");
        }

        List<Place> places = placeService.getPlacesWithinArea(area);
        return ResponseEntity.ok(places);
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



    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        Place savedPlace = placeService.savePlace(place);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(
            @PathVariable Integer id,
            @RequestBody Place updatedPlace) {
        Place savedPlace = placeService.updatePlace(id,updatedPlace);
        return ResponseEntity.ok(savedPlace);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Integer id) {
        placeService.deletePlaceById(id);
        return ResponseEntity.noContent().build();
    }
}
