package com.lojister.model.dto.request;

import lombok.Data;

import javax.validation.constraints.DecimalMin;

@Data
public class CommissionRateDto {

    private Long companyId;

    @DecimalMin(value = "0.0",message ="{lojister.constraint.commissionRate.MinValue.message}")
    private Double commissionRate;
}
