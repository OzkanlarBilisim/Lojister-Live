package com.lojister.business.abstracts;

import com.lojister.model.dto.SavedClientAdvertisementResponseDto;
import com.lojister.model.dto.SavedClientAdvertisementSaveDto;

import java.util.List;

public interface SavedClientAdvertisementService {

    void save(SavedClientAdvertisementSaveDto savedClientAdvertisementSaveDto);

    SavedClientAdvertisementResponseDto getById(Long savedClientAdvertisementId);

    void deleteById(Long savedClientAdvertisementId);

    List<SavedClientAdvertisementResponseDto> getMySavedClientAdvertisements();

}
