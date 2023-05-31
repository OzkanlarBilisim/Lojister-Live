package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.AllRatingCountDto;
import com.lojister.model.dto.RatingClientTransportProcessDto;
import com.lojister.model.enums.RatingStatus;
import com.lojister.model.enums.SortingType;
import com.lojister.model.enums.TransportProcessStatus;
import com.lojister.core.exception.CompanyRatingNotExistException;
import com.lojister.core.exception.DuplicateRatingException;
import com.lojister.core.exception.TransportProcessStatusException;
import com.lojister.core.exception.WrongSortingType;
import com.lojister.mapper.RatingClientTransportProcessMapper;
import com.lojister.model.entity.Company;
import com.lojister.model.entity.RatingClientTransportProcess;
import com.lojister.model.entity.client.ClientTransportProcess;
import com.lojister.repository.transport.RatingClientTransportProcessRepository;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.business.abstracts.CompanyService;
import com.lojister.business.abstracts.RatingClientTransportProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingClientTransportProcessServiceImpl implements RatingClientTransportProcessService {

    private final RatingClientTransportProcessRepository ratingClientTransportProcessRepository;
    private final RatingClientTransportProcessMapper ratingClientTransportProcessMapper;
    private final ClientTransportProcessService clientTransportProcessService;
    private final CompanyService companyService;

    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_CREATED_DATE_TIME = "createdDateTime";



    @Override
    public void checkTransportProcessStatus(ClientTransportProcess clientTransportProcess) {

        if (clientTransportProcess.getTransportProcessStatus() != TransportProcessStatus.COMPLETED) {
            throw new TransportProcessStatusException(Translator.toLocale("lojister.ratingClientTransportProcess.TransportProcessStatusException"));
        }
    }

    @Override
    public void checkDuplicateRating(ClientTransportProcess clientTransportProcess) {

        Long clientTransportProcessId = clientTransportProcess.getId();

        Long clientId = clientTransportProcess.getAcceptedClientAdvertisementBid().getClientAdvertisement().getClient().getId();

        Optional<RatingClientTransportProcess> ratingClientTransportProcess = ratingClientTransportProcessRepository.findByClientTransportProcess_IdAndClient_Id(clientTransportProcessId, clientId);

        if (ratingClientTransportProcess.isPresent()) {
            throw new DuplicateRatingException(Translator.toLocale("lojister.ratingClientTransportProcess.DuplicateRatingException"));
        }
    }


    @Override
    public Boolean saveRating(String comment, BigDecimal rating, Long clientTransportProcessId) {

        ClientTransportProcess clientTransportProcess = clientTransportProcessService.findDataById(clientTransportProcessId);

        checkTransportProcessStatus(clientTransportProcess);
        checkDuplicateRating(clientTransportProcess);
        clientTransportProcess.setIsRating(Boolean.TRUE);
        clientTransportProcess = clientTransportProcessService.saveRepo(clientTransportProcess);

        Company company = clientTransportProcess.getAcceptedClientAdvertisementBid().getDriverBidder().getCompany();

        BigDecimal averageRating;
        BigDecimal totalRating;
        if (company.getNumberOfRating() == 0) {
            company.setRating(rating.doubleValue());
        } else {

            totalRating = BigDecimal.valueOf(company.getRating()).multiply(BigDecimal.valueOf(company.getNumberOfRating())).setScale(2, RoundingMode.HALF_UP);

            averageRating = (totalRating.add(rating)).divide(BigDecimal.valueOf(company.getNumberOfRating() + 1), 2, RoundingMode.HALF_UP);

            company.setRating(averageRating.doubleValue());
        }
        company.setNumberOfRating(company.getNumberOfRating() + 1);
        companyService.saveRepo(company);

        RatingClientTransportProcess ratingClientTransportProcess = new RatingClientTransportProcess();
        ratingClientTransportProcess.setClientTransportProcess(clientTransportProcess);
        ratingClientTransportProcess.setRating(rating.longValue());
        ratingClientTransportProcess.setComment(comment);
        ratingClientTransportProcess.setRatingStatus(RatingStatus.APPROVED);
        ratingClientTransportProcessRepository.save(ratingClientTransportProcess);
        return Boolean.TRUE;
    }


    @Override
    public List<RatingClientTransportProcessDto> findAllRatingByCompanyId(Long companyId, SortingType sortingType) {

        Company company = companyService.findDataById(companyId);

        if (sortingType == SortingType.DATE_DESC) {
            return ratingClientTransportProcessMapper.entityListToDtoList(ratingClientTransportProcessRepository.findRatingByCompanyId(company.getId(), Sort.by(Sort.Direction.DESC, COLUMN_CREATED_DATE_TIME)));
        } else if (sortingType == SortingType.DATE_ASC) {
            return ratingClientTransportProcessMapper.entityListToDtoList(ratingClientTransportProcessRepository.findRatingByCompanyId(company.getId(), Sort.by(Sort.Direction.ASC, COLUMN_CREATED_DATE_TIME)));
        } else if (sortingType == SortingType.RATING_DESC) {
            return ratingClientTransportProcessMapper.entityListToDtoList(ratingClientTransportProcessRepository.findRatingByCompanyId(company.getId(), Sort.by(Sort.Direction.DESC, COLUMN_RATING)));
        } else if (sortingType == SortingType.RATING_ASC) {
            return ratingClientTransportProcessMapper.entityListToDtoList(ratingClientTransportProcessRepository.findRatingByCompanyId(company.getId(), Sort.by(Sort.Direction.ASC, COLUMN_RATING)));
        } else {

            throw new WrongSortingType(Translator.toLocale("lojister.ratingClientTransportProcess.WrongSortingType"));
        }

    }

    @Override
    public AllRatingCountDto allRatingCountByCompanyId(Long companyId) {

        Company company = companyService.findDataById(companyId);

        AllRatingCountDto allRatingCountDto = new AllRatingCountDto();
        allRatingCountDto.setAverageRating(company.getRating());
        allRatingCountDto.setTotalRatingCount(company.getNumberOfRating());
        allRatingCountDto.setFiveStarCount(ratingClientTransportProcessRepository.ratingCountByCompany_IdAndRatingScore(company.getId(), 5L));
        allRatingCountDto.setFourStarCount(ratingClientTransportProcessRepository.ratingCountByCompany_IdAndRatingScore(company.getId(), 4L));
        allRatingCountDto.setThreeStarCount(ratingClientTransportProcessRepository.ratingCountByCompany_IdAndRatingScore(company.getId(), 3L));
        allRatingCountDto.setTwoStarCount(ratingClientTransportProcessRepository.ratingCountByCompany_IdAndRatingScore(company.getId(), 2L));
        allRatingCountDto.setOneStarCount(ratingClientTransportProcessRepository.ratingCountByCompany_IdAndRatingScore(company.getId(), 1L));

        return allRatingCountDto;
    }


    @Override
    public Page<RatingClientTransportProcessDto> findByCompanyIdAndRatingList(Long companyId, List<Long> ratingList, Pageable pageable) {

        Company company = companyService.findDataById(companyId);
        companyRatingIsExistCheck(company);
        Pageable customPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<RatingClientTransportProcess> ratingClientTransportProcess=ratingClientTransportProcessRepository.findByCompany_IdAndRatingList(company.getId(), ratingList, pageable);
        List<RatingClientTransportProcessDto> ratingClientTransportProcessDtoList=ratingClientTransportProcessMapper.entityListToDtoList(ratingClientTransportProcess.getContent());
        Page<RatingClientTransportProcessDto> ratingClientTransportProcessDtoPage=new PageImpl<>(ratingClientTransportProcessDtoList,ratingClientTransportProcess.getPageable(),ratingClientTransportProcess.getTotalElements());
        return ratingClientTransportProcessDtoPage;
    }

    @Override
    public void companyRatingIsExistCheck(Company company) {

        if (company.getNumberOfRating() == 0) {
            throw new CompanyRatingNotExistException(Translator.toLocale("lojister.ratingClientTransportProcess.CompanyRatingNotExistException"));
        }
    }

}
