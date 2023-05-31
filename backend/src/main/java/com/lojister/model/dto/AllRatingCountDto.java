package com.lojister.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllRatingCountDto {

    private Long fiveStarCount;

    private Long fourStarCount;

    private Long threeStarCount;

    private Long twoStarCount;

    private Long oneStarCount;

    private Double averageRating;

    private Long totalRatingCount;

}
