package com.example.pricetag.responses;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto {
  private String message;
  @Builder.Default
  private Map<String, ?> data = new HashMap<>();
  private String statusCode;
}
