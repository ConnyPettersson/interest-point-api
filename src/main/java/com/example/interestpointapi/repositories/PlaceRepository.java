package com.example.interestpointapi.repositories;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
    List<Place> findByIsPrivate(Boolean isPrivate);
    List<Place> findByIsPrivateFalseAndCategory(Category category);
    List<Place> findByUserId(Integer userId);
    @Query(value = "SELECT * FROM place WHERE ST_Within(coordinates, ST_GeomFromText(?1, 4326)) = 1", nativeQuery = true)
    List<Place> findByCoordinatesWithinNative(String wktPolygon);

}
