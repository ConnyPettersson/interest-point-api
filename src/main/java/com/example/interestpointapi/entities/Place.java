package com.example.interestpointapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.geolatte.geom.codec.Wkt;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

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

    private String description;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point<G2D> coordinates;

    public void setCoordinates(String wkt) {
        try {
            this.coordinates = (Point<G2D>) Wkt.fromWkt(wkt);
            if (this.coordinates.getSRID() == 0) {
                this.coordinates = DSL.point(CoordinateReferenceSystems.WGS84, this.coordinates.getPosition());
            }
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
