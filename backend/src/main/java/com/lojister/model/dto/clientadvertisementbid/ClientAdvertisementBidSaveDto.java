package com.lojister.model.dto.clientadvertisementbid;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAdvertisementBidSaveDto {

    @DecimalMin(value = "0.0",message ="{lojister.constraint.bid.MinValue.message}")
    private Double bid;
    @Size(max = 255,message = "Açıklama en fazla 255 karakter olabilir.")
    private String explanation;
    @FutureOrPresent(message = "Geçmiş bir tarih seçemezsiniz.")
    private LocalDateTime expiration;


}
