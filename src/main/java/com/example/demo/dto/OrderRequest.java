package com.example.demo.dto;

import lombok.Data;
import java.util.Set;
import javax.validation.constraints.*;

@Data
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Item IDs cannot be empty")
    private Set<@NotNull Long> itemIds;
}
