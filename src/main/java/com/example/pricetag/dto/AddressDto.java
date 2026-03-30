package com.example.pricetag.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String street;
    private String city;
    private String postalCode;
}
