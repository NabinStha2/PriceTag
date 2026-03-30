package com.example.pricetag.dto;


import com.example.pricetag.entity.User;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingReviewDto {
    private int rating;
    private String review;
    private User user;
}
