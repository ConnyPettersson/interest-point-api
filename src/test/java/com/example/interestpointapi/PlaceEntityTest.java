package com.example.interestpointapi;

import com.example.interestpointapi.entities.Category;
import com.example.interestpointapi.entities.Place;
import jakarta.persistence.EntityManager;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PlaceEntityTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void testPlaceEntityPersistence() {

        entityManager.createQuery("DELETE FROM Place").executeUpdate();
        entityManager.createQuery("DELETE FROM Category").executeUpdate();

        // Skapa och spara en kategori
        Category category = new Category();
        category.setName("Test Category");
        category.setSymbol("⭐");
        category.setDescription("A category for testing");
        entityManager.persist(category);

        // Skapa en plats med Geolatte-koordinationer
        Place place = new Place();
        place.setName("Test Place");
        place.setCategory(category);
        place.setUserId(1);
        place.setPrivate(false);
        place.setDescription("A test place for JPA integration testing");

        // Använd Geolatte för att skapa geometrin
        String wellKnownText = "POINT(12.34 56.78)";
        Point<?> coordinates = (Point<?>) Wkt.fromWkt(wellKnownText);
        place.setCoordinates(coordinates);

        entityManager.persist(place);
        entityManager.flush();

        // Kontrollera att platsen sparats korrekt
        List<Place> places = entityManager.createQuery("SELECT p FROM Place p", Place.class).getResultList();
        assertThat(places).isNotNull();
        assertThat(places).hasSize(1);

        Place savedPlace = places.get(0);
        assertThat(savedPlace.getName()).isEqualTo("Test Place");

        // Validera att geometrin sparats korrekt
        assertThat(Wkt.toWkt(savedPlace.getCoordinates())).isEqualTo(wellKnownText);
    }
}
