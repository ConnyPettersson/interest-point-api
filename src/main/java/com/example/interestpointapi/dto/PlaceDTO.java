package com.example.interestpointapi.dto;

import com.example.interestpointapi.validation.CreateValidation;
import com.example.interestpointapi.validation.ExistingCategoryId;
import com.example.interestpointapi.validation.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDTO {
    @NotNull(message = "Name is required", groups = CreateValidation.class)
    @NotBlank(message = "Name must not be blank", groups = CreateValidation.class)
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters", groups = {CreateValidation.class, UpdateValidation.class})
    private String name;

    @Positive(message = "Category ID must be a positive number", groups = CreateValidation.class)
    @NotNull(message = "Category ID is required", groups = CreateValidation.class)
    @ExistingCategoryId(groups = CreateValidation.class)
    private Integer categoryId;

    @JsonProperty("isPrivate")
    private boolean isPrivate;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Coordinates are required", groups = CreateValidation.class)
    private String coordinates;
}
