package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.TrailerFeatureDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.TrailerFeatureMapper;
import com.lojister.model.entity.adminpanel.TrailerFeature;
import com.lojister.repository.trailer.TrailerFeatureRepository;
import com.lojister.business.abstracts.dynamic.TrailerFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TrailerFeatureServiceImpl implements TrailerFeatureService {

    private final TrailerFeatureRepository trailerFeatureRepository;
    private final TrailerFeatureMapper trailerFeatureMapper;

    private static final String TRAILER_FEATURE_NOTFOUND_MESSAGE = "Dorse Özelliği Bulunamadı.";

    @Autowired
    public TrailerFeatureServiceImpl(TrailerFeatureRepository trailerFeatureRepository,
                                     TrailerFeatureMapper trailerFeatureMapper) {
        this.trailerFeatureRepository = trailerFeatureRepository;
        this.trailerFeatureMapper = trailerFeatureMapper;
    }


    @Override
    public Set<TrailerFeature> findSetTrailerFeaturesByIdList(List<Long> idList) {

        if (idList.isEmpty()) {
            return new HashSet<>(trailerFeatureRepository.findByDynamicStatus(DynamicStatus.ACTIVE));
        }

        Set<TrailerFeature> trailerFeatureSet = trailerFeatureRepository.findByIdIn(idList);
        if (!(trailerFeatureSet.isEmpty())) {
            return trailerFeatureSet;
        } else {
            throw new EntityNotFoundException(TRAILER_FEATURE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerFeature> findSetTrailerFeaturesByFeatureNameList(List<String> featureNameList) {

        Set<TrailerFeature> trailerFeatureSet = trailerFeatureRepository.findByFeatureNameInIgnoreCase(featureNameList);
        if (!(trailerFeatureSet.isEmpty())) {
            return trailerFeatureSet;
        } else {
            throw new EntityNotFoundException(TRAILER_FEATURE_NOTFOUND_MESSAGE);
        }
    }


    @Override
    public void duplicateFeatureNameCheck(String featureName) {
        Optional<TrailerFeature> trailerFeature = trailerFeatureRepository.findByFeatureNameIgnoreCase(featureName);
        if (trailerFeature.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Dorse Özelliği Zaten Mevcuttur.");
        }
    }


    @Override
    @OnlyAdmin
    public TrailerFeatureDto save(TrailerFeatureDto trailerFeatureDto) {

        duplicateFeatureNameCheck(trailerFeatureDto.getFeatureName());
        return trailerFeatureMapper.entityToDto(trailerFeatureRepository.save(TrailerFeature.create(trailerFeatureDto.getFeatureName(), trailerFeatureDto.getEngFeatureName())));

    }

    @Override
    @OnlyAdmin
    public TrailerFeatureDto update(Long id, TrailerFeatureDto trailerFeatureDto) {

        TrailerFeature trailerFeature = findDataById(id);

        trailerFeature.setFeatureName(trailerFeatureDto.getFeatureName());
        trailerFeature.setEngFeatureName(trailerFeatureDto.getEngFeatureName());
        return trailerFeatureMapper.entityToDto(trailerFeatureRepository.save(trailerFeature));
    }

    @Override
    public TrailerFeatureDto getById(Long id) {

        TrailerFeature trailerFeature = findDataById(id);
        return trailerFeatureMapper.entityToDto(trailerFeature);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        trailerFeatureRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<TrailerFeatureDto> getAll() {
        return trailerFeatureMapper.entityListToDtoList(trailerFeatureRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        TrailerFeature trailerFeature = findDataById(id);

        trailerFeature.setDynamicStatus(DynamicStatus.ACTIVE);
        trailerFeatureRepository.save(trailerFeature);
    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        TrailerFeature trailerFeature = findDataById(id);

        trailerFeature.setDynamicStatus(DynamicStatus.PASSIVE);
        trailerFeatureRepository.save(trailerFeature);
    }

    private List<TrailerFeature> engNullCheck(List<TrailerFeature> trailerFeatureList) {
        trailerFeatureList.stream().filter(trailerFeature -> trailerFeature.getEngFeatureName() == null).forEach(trailerFeature -> trailerFeature.setEngFeatureName(trailerFeature.getFeatureName()));
        return trailerFeatureList;
    }

    @Override
    public List<TrailerFeatureDto> getActive() {
        return trailerFeatureMapper.entityListToDtoList(engNullCheck(trailerFeatureRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<TrailerFeatureDto> getPassive() {
        return trailerFeatureMapper.entityListToDtoList(trailerFeatureRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }

    @Override
    public TrailerFeature findDataById(Long id) {

        Optional<TrailerFeature> trailerFeature = trailerFeatureRepository.findById(id);

        if (trailerFeature.isPresent()) {
            return trailerFeature.get();

        } else {
            throw new EntityNotFoundException(TRAILER_FEATURE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerFeature> findAll() {
        return trailerFeatureRepository.getAll(DynamicStatus.ACTIVE);
    }


}
