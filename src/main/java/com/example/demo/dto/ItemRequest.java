package com.example.demo.dto;

import lombok.Data;
import java.util.Set;
import javax.validation.constraints.*;

@Data
public class ItemRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 255, message = "Description can't exceed 255 characters")
    private String description;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @NotEmpty(message = "At least one category is required")
    private Set<@NotBlank String> categoryNames;
}
