package com.example.interestpointapi.entities;

import com.example.interestpointapi.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometry;
import org.geolatte.geom.Point;
import org.geolatte.geom.codec.Wkt;

import java.time.LocalDateTime;

@Entity
@Table(name = "place")
@Getter
@Setter
@NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(groups = OnCreate.class, message = "Name is required")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(groups = OnCreate.class, message = "Category is required")
    private Category category;

    @NotNull(groups = OnCreate.class, message = "User ID is required")
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "is_private", nullable = false)
    @JsonProperty("isPrivate")
    private boolean isPrivate;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(groups = OnCreate.class, message = "Coordinates are required")
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point coordinates;


    public void setCoordinates(String wkt) {
        try {
            this.coordinates = (Point<G2D>) Wkt.fromWkt(wkt);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WKT format: " + wkt, e);
        }
    }


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}