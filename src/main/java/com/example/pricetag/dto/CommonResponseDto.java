package com.example.pricetag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON response
public class CommonResponseDto<T> {
    private String message;       // e.g., "Product created successfully"
    private T data;               // actual payload (Product, List<Order>, etc.)
    private Boolean success;      // true if operation succeeded
    private Integer status;       // HTTP status code
    private PaginatedResponseDto pagination;
    private Map<String, Object> meta; // optional metadata (pagination, filters, etc.)
}