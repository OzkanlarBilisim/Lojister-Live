package com.lojister.model.entity;

import com.lojister.model.enums.RatingStatus;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.entity.client.ClientTransportProcess;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingClientTransportProcess extends AbstractTimestampEntity {

    @OneToOne
    private ClientTransportProcess clientTransportProcess;

    private String comment;

    private Long rating;

    @Enumerated(EnumType.STRING)
    private RatingStatus ratingStatus;

}
