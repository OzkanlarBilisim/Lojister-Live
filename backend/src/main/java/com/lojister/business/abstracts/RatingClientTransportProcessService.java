package com.lojister.business.abstracts;

import com.lojister.model.dto.AllRatingCountDto;
import com.lojister.model.dto.RatingClientTransportProcessDto;
import com.lojister.model.entity.Company;
import com.lojister.model.enums.SortingType;
import com.lojister.model.entity.client.ClientTransportProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface RatingClientTransportProcessService {

    void checkTransportProcessStatus(ClientTransportProcess clientTransportProcess);

    void checkDuplicateRating(ClientTransportProcess clientTransportProcess);

    Boolean saveRating(String comment, BigDecimal rating, Long clientTransportProcessId);

    List<RatingClientTransportProcessDto> findAllRatingByCompanyId(Long companyId, SortingType sortingType);

    AllRatingCountDto allRatingCountByCompanyId(Long companyId);

    Page<RatingClientTransportProcessDto> findByCompanyIdAndRatingList(Long companyId, List<Long> ratingList, Pageable pageable);

    void companyRatingIsExistCheck(Company company);

}
