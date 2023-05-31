package com.lojister.model.dto;

import com.lojister.model.dto.base.BaseDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.model.enums.RatingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingClientTransportProcessDto extends BaseDto {

    private ClientTransportProcessDto clientTransportProcess;

    private String comment;

    @DecimalMax(value = "5.0",message ="{lojister.constraint.rating.MaxValue.message}")
    @DecimalMin(value = "1.0",message ="{lojister.constraint.rating.MinValue.message}")
    private Long rating;

    private RatingStatus ratingStatus;

    private LocalDateTime createdDateTime;

}
