package com.example.pricetag.responses;

import java.util.Optional;

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
  private Optional<Object> data;
  private String statusCode;
}
