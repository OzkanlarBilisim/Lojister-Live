package com.lojister.business.concretes;

import com.lojister.business.abstracts.SavedClientAdvertisementService;
import com.lojister.business.abstracts.dynamic.*;
import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.SavedClientAdvertisementResponseDto;
import com.lojister.model.dto.SavedClientAdvertisementSaveDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.core.exception.UndefinedClientAdvertisementTypeException;
import com.lojister.mapper.SavedClientAdvertisementMapper;
import com.lojister.model.entity.Recipient;
import com.lojister.model.entity.SavedClientAdvertisement;
import com.lojister.model.entity.adminpanel.*;
import com.lojister.model.entity.client.Client;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.repository.advertisement.SavedClientAdvertisementRepository;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.core.validator.formatter.PhoneFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SavedClientAdvertisementServiceImpl implements SavedClientAdvertisementService {

    private final SavedClientAdvertisementRepository savedClientAdvertisementRepository;
    private final SavedClientAdvertisementMapper savedClientAdvertisementMapper;
    private final SecurityContextUtil securityContextUtil;
    private final VehicleTypeService vehicleTypeService;
    private final TrailerTypeService trailerTypeService;
    private final TrailerFloorTypeService trailerFloorTypeService;
    private final TrailerFeatureService trailerFeatureService;
    private final PackagingTypeService packagingTypeService;
    private final CargoTypeService cargoTypeService;
    private final LoadTypeService loadTypeService;
    private final CurrencyUnitService currencyUnitService;
    private final PhoneFormatter phoneFormatter;


    public void checkedClientAdvertisementType(SavedClientAdvertisementSaveDto savedClientAdvertisementSaveDto) {

        if (!(savedClientAdvertisementSaveDto.getClientAdvertisementType() == ClientAdvertisementType.PARTIAL
                || savedClientAdvertisementSaveDto.getClientAdvertisementType() == ClientAdvertisementType.FTL)) {

            throw new UndefinedClientAdvertisementTypeException(Translator.toLocale("lojister.savedClientAdvertisement.UndefinedClientAdvertisementTypeException"));
        }
    }


    @Override
    public void save(SavedClientAdvertisementSaveDto savedClientAdvertisementSaveDto) {

        Client currentClient = securityContextUtil.getCurrentClient();

        checkedClientAdvertisementType(savedClientAdvertisementSaveDto);

        SavedClientAdvertisement savedClientAdvertisement = new SavedClientAdvertisement();
        savedClientAdvertisement.setAdvertisementName(savedClientAdvertisementSaveDto.getAdvertisementName());
        savedClientAdvertisement.setClient(currentClient);
        savedClientAdvertisement.setClientAdvertisementType(savedClientAdvertisementSaveDto.getClientAdvertisementType());

        Set<TrailerType> trailerTypeSet = trailerTypeService.findSetTrailerTypesByIdList(savedClientAdvertisementSaveDto.getTrailerTypeIdList());
        savedClientAdvertisement.setTrailerTypeSet(trailerTypeSet);


        Set<TrailerFloorType> trailerFloorTypeSet = trailerFloorTypeService.findSetTrailerFloorTypesByIdList(savedClientAdvertisementSaveDto.getTrailerFloorTypeIdList());
        savedClientAdvertisement.setTrailerFloorTypeSet(trailerFloorTypeSet);


        Set<TrailerFeature> trailerFeatureSet = trailerFeatureService.findSetTrailerFeaturesByIdList(savedClientAdvertisementSaveDto.getTrailerFeatureIdList());
        savedClientAdvertisement.setTrailerFeatureSet(trailerFeatureSet);

        savedClientAdvertisement.setStartingAddress(savedClientAdvertisementSaveDto.getStartingAddress());
        savedClientAdvertisement.setDueAddress(savedClientAdvertisementSaveDto.getDueAddress());

        Recipient startRecipient = savedClientAdvertisementSaveDto.getStartRecipient();
        String formattedNumber = phoneFormatter.format(startRecipient.getPhoneNumber());
        startRecipient.setPhoneNumber(formattedNumber);
        savedClientAdvertisement.setStartRecipient(startRecipient);

        Recipient dueRecipient = savedClientAdvertisementSaveDto.getDueRecipient();
        String formattedNumber2 = phoneFormatter.format(dueRecipient.getPhoneNumber());
        dueRecipient.setPhoneNumber(formattedNumber2);
        savedClientAdvertisement.setDueRecipient(dueRecipient);


        PackagingType packagingType = packagingTypeService.findDataById(savedClientAdvertisementSaveDto.getPackagingTypeId());
        savedClientAdvertisement.setPackagingType(packagingType);

        Set<CargoType> cargoTypeSet = cargoTypeService.findSetCargoTypesByIdList(savedClientAdvertisementSaveDto.getCargoTypeIdList());
        savedClientAdvertisement.setCargoTypeSet(cargoTypeSet);

        savedClientAdvertisement.setTonnage(savedClientAdvertisementSaveDto.getTonnage());

        Set<LoadType> loadTypes = loadTypeService.findSetLoadTypeByIdList(savedClientAdvertisementSaveDto.getLoadTypeIdList());
        savedClientAdvertisement.setLoadTypeSet(loadTypes);

        savedClientAdvertisement.setGoodsPrice(savedClientAdvertisementSaveDto.getGoodsPrice());
        savedClientAdvertisement.setIsPorter(savedClientAdvertisementSaveDto.getIsPorter());
        savedClientAdvertisement.setIsStacking(savedClientAdvertisementSaveDto.getIsStacking());

        savedClientAdvertisement.setDocumentNo(savedClientAdvertisementSaveDto.getDocumentNo());
        savedClientAdvertisement.setExplanation(savedClientAdvertisementSaveDto.getExplanation());

        CurrencyUnit currencyUnit = currencyUnitService.findDataById(savedClientAdvertisementSaveDto.getCurrencyUnitId());
        savedClientAdvertisement.setCurrencyUnit(currencyUnit);

        savedClientAdvertisement.setVolume(savedClientAdvertisementSaveDto.getVolume());
        savedClientAdvertisement.setDesi(savedClientAdvertisementSaveDto.getDesi());
        savedClientAdvertisement.setLdm(savedClientAdvertisementSaveDto.getLdm());

        if (savedClientAdvertisementSaveDto.getClientAdvertisementType() == ClientAdvertisementType.PARTIAL) {

            savedClientAdvertisement.setPiece(savedClientAdvertisementSaveDto.getPiece());
            savedClientAdvertisement.setWidth(savedClientAdvertisementSaveDto.getWidth());
            savedClientAdvertisement.setLength(savedClientAdvertisementSaveDto.getLength());
            savedClientAdvertisement.setHeight(savedClientAdvertisementSaveDto.getHeight());

        } else if (savedClientAdvertisementSaveDto.getClientAdvertisementType() == ClientAdvertisementType.FTL) {

            Set<VehicleType> vehicleTypeSet = vehicleTypeService.findSetVehicleTypesByIdList(savedClientAdvertisementSaveDto.getVehicleTypeIdList());
            savedClientAdvertisement.setVehicleTypeSet(vehicleTypeSet);

            savedClientAdvertisement.setVehicleCount(savedClientAdvertisementSaveDto.getVehicleCount());

        }

        savedClientAdvertisementRepository.save(savedClientAdvertisement);


    }

    @Override
    public SavedClientAdvertisementResponseDto getById(Long savedClientAdvertisementId) {

        Optional<SavedClientAdvertisement> savedClientAdvertisement = savedClientAdvertisementRepository.findById(savedClientAdvertisementId);

        if (!savedClientAdvertisement.isPresent()) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.savedClientAdvertisement.EntityNotFoundException"));
        }
        if (!savedClientAdvertisement.get().getClient().getId().equals(securityContextUtil.getCurrentClient().getId())) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.savedClientAdvertisement.UnauthorizedTransactionException"));
        }

        return savedClientAdvertisementMapper.entityToDto(savedClientAdvertisement.get());

    }

    @Override
    public void deleteById(Long savedClientAdvertisementId) {

        Optional<SavedClientAdvertisement> savedClientAdvertisement = savedClientAdvertisementRepository.findById(savedClientAdvertisementId);

        if (!savedClientAdvertisement.isPresent()) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.savedClientAdvertisement.EntityNotFoundException"));
        }
        if (!savedClientAdvertisement.get().getClient().getId().equals(securityContextUtil.getCurrentClient().getId())) {
            throw new UnauthorizedTransactionException(Translator.toLocale("lojister.savedClientAdvertisement.UnauthorizedTransactionException"));
        }

        savedClientAdvertisementRepository.deleteById(savedClientAdvertisement.get().getId());

    }


    @Override
    public List<SavedClientAdvertisementResponseDto> getMySavedClientAdvertisements() {

        return savedClientAdvertisementMapper.entityListToDtoList(savedClientAdvertisementRepository.findByClient_IdOrderByIdDesc(securityContextUtil.getCurrentClient().getId()));
    }
}
