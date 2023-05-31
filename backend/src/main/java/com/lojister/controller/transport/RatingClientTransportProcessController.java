package com.lojister.controller.transport;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.AllRatingCountDto;
import com.lojister.model.dto.RatingClientTransportProcessDto;
import com.lojister.model.dto.request.SaveRatingDto;
import com.lojister.model.enums.SortingType;
import com.lojister.business.abstracts.RatingClientTransportProcessService;
import com.lojister.core.api.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ratingClientTransportProcess")
@CrossOrigin
@Authenticated
public class RatingClientTransportProcessController {

    private final RatingClientTransportProcessService ratingClientTransportProcessService;

    @Autowired
    public RatingClientTransportProcessController(RatingClientTransportProcessService ratingClientTransportProcessService) {
        this.ratingClientTransportProcessService = ratingClientTransportProcessService;
    }


    @PostMapping()
    public ResponseEntity<Boolean> saveRating(@Valid @RequestBody SaveRatingDto saveRatingDto) {

        return ResponseEntity.ok(ratingClientTransportProcessService.saveRating(saveRatingDto.getComment(), saveRatingDto.getRating(), saveRatingDto.getClientTransportProcessId()));

    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<RatingClientTransportProcessDto>> findByCompanyIdAndRatingList(@PathVariable("companyId") Long companyId, @RequestParam(value = "ratingList", defaultValue = "1,2,3,4,5", required = false) List<Long> ratingList, Pageable pageable) {

        return ResponseEntity.ok(ratingClientTransportProcessService.findByCompanyIdAndRatingList(companyId, ratingList, pageable));

    }

    @GetMapping("/count/company/{companyId}")
    public ResponseEntity<AllRatingCountDto> allRatingCountByCompanyId(@PathVariable("companyId") Long companyId) {

        return ResponseEntity.ok(ratingClientTransportProcessService.allRatingCountByCompanyId(companyId));

    }


}
