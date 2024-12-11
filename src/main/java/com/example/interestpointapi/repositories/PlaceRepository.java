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
    @Query("SELECT p FROM Place p WHERE ST_Within(p.coordinates, :area) = true")
    List<Place> findByCoordinatesWithin(@Param("area") Geometry<?> area);
    @Query("SELECT p FROM Place p WHERE ST_Distance(ST_Transform(p.coordinates, 4326), :center) <= :radius")
    List<Place> findByCoordinatesWithinRadius(@Param("center") Point<G2D> center, @Param("radius") double radius);
}
