package com.example.interestpointapi.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 10, message = "Symbol must be less than 10 characters")
    private String symbol;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    public Category(Integer id) {
        this.id = id;
    }
}
