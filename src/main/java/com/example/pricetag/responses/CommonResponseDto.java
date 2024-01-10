package com.example.pricetag.responses;

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
  private Map<String, Integer> data;
  private String statusCode;
}
