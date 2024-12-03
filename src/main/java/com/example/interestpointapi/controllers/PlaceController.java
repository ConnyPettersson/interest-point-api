package com.example.interestpointapi.controllers;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import com.example.interestpointapi.services.PlaceService;
import org.geolatte.geom.Geometry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/places")

public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Place>> getPlacesByPrivacy(@RequestParam("isPrivate") Boolean isPrivate) {
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
        List<Place> userPlaces = placeService.getPlacesByUserId(userId);
        return ResponseEntity.ok(userPlaces);
    }

    @GetMapping("/area")
    public ResponseEntity <List<Place>> getPlacesWithinAreas(@RequestBody Geometry area) {
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null");
        }

        List<Place> places = placeService.getPlacesWithinArea(area);
        return ResponseEntity.ok(places);
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
