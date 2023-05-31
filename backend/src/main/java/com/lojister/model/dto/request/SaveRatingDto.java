package com.lojister.model.dto.request;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class SaveRatingDto {

    private Long clientTransportProcessId;

    private String comment;

    @DecimalMax(value = "5.0", message = "{lojister.constraint.rating.MaxValue.message}")
    @DecimalMin(value = "1.0", message = "{lojister.constraint.rating.MinValue.message}")
    private BigDecimal rating;

}
