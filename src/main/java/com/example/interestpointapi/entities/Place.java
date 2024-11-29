package com.example.interestpointapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.Geometry;
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

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "is_private", nullable = false)
    @JsonProperty("isPrivate")
    private boolean isPrivate;


    @Column
    private String description;

    @Column(nullable = false, columnDefinition = "geometry")
    private Geometry<?> coordinates;

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

    public void setCoordinatesFromWkt(String wkt) {
        try {
            this.coordinates = Wkt.fromWkt(wkt);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WKT format for coordinates", e);
        }
    }
}
