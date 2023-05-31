package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.TrailerFloorTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.TrailerFloorTypeMapper;
import com.lojister.model.entity.adminpanel.TrailerFloorType;
import com.lojister.repository.trailer.TrailerFloorTypeRepository;
import com.lojister.business.abstracts.dynamic.TrailerFloorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TrailerFloorTypeServiceImpl implements TrailerFloorTypeService {

    private final TrailerFloorTypeRepository trailerFloorTypeRepository;
    private final TrailerFloorTypeMapper trailerFloorTypeMapper;

    private static final String TRAILER_FLOOR_TYPE_NOTFOUND_MESSAGE = "Dorse Zemin Tipi Bulunamadı.";

    @Autowired
    public TrailerFloorTypeServiceImpl(TrailerFloorTypeRepository trailerFloorTypeRepository,
                                       TrailerFloorTypeMapper trailerFloorTypeMapper) {
        this.trailerFloorTypeRepository = trailerFloorTypeRepository;
        this.trailerFloorTypeMapper = trailerFloorTypeMapper;
    }


    @Override
    public Set<TrailerFloorType> findSetTrailerFloorTypesByIdList(List<Long> idList) {

        if (idList.get(0) == 0L) {
            return new HashSet<>(trailerFloorTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE));
        }

        Set<TrailerFloorType> trailerFloorTypeSet = trailerFloorTypeRepository.findByIdInAndDynamicStatus(idList, DynamicStatus.ACTIVE);
        if (!(trailerFloorTypeSet.isEmpty())) {
            return trailerFloorTypeSet;
        } else {
            throw new EntityNotFoundException(TRAILER_FLOOR_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerFloorType> findSetTrailerFloorTypesByTypeNameList(List<String> typeNameList) {

        Set<TrailerFloorType> trailerFloorTypeSet = trailerFloorTypeRepository.findByTypeNameInIgnoreCase(typeNameList);
        if (!(trailerFloorTypeSet.isEmpty())) {
            return trailerFloorTypeSet;
        } else {
            throw new EntityNotFoundException(TRAILER_FLOOR_TYPE_NOTFOUND_MESSAGE);
        }
    }


    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<TrailerFloorType> trailerFloorType = trailerFloorTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (trailerFloorType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Dorse Zemin Tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public TrailerFloorType findDataById(Long id) {

        Optional<TrailerFloorType> trailerFloorType = trailerFloorTypeRepository.findById(id);

        if (trailerFloorType.isPresent()) {

            return trailerFloorType.get();
        } else {
            throw new EntityNotFoundException(TRAILER_FLOOR_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerFloorType> findAll() {
        return trailerFloorTypeRepository.getAll(DynamicStatus.ACTIVE);
    }


    @Override
    @OnlyAdmin
    public TrailerFloorTypeDto save(TrailerFloorTypeDto trailerFloorTypeDto) {

        duplicateTypeNameCheck(trailerFloorTypeDto.getTypeName());
        return trailerFloorTypeMapper.entityToDto(trailerFloorTypeRepository.save(TrailerFloorType.create(trailerFloorTypeDto.getTypeName(), trailerFloorTypeDto.getEngTypeName())));

    }

    @Override
    @OnlyAdmin
    public TrailerFloorTypeDto update(Long id, TrailerFloorTypeDto trailerFloorTypeDto) {

        TrailerFloorType trailerFloorType = findDataById(id);

        trailerFloorType.setTypeName(trailerFloorTypeDto.getTypeName());
        trailerFloorType.setEngTypeName(trailerFloorTypeDto.getEngTypeName());
        return trailerFloorTypeMapper.entityToDto(trailerFloorTypeRepository.save(trailerFloorType));

    }

    @Override
    public TrailerFloorTypeDto getById(Long id) {

        TrailerFloorType trailerFloorType = findDataById(id);

        return trailerFloorTypeMapper.entityToDto(trailerFloorType);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);

        trailerFloorTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<TrailerFloorTypeDto> getAll() {
        return trailerFloorTypeMapper.entityListToDtoList(trailerFloorTypeRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        TrailerFloorType trailerFloorType = findDataById(id);

        trailerFloorType.setDynamicStatus(DynamicStatus.ACTIVE);
        trailerFloorTypeRepository.save(trailerFloorType);
    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        TrailerFloorType trailerFloorType = findDataById(id);

        trailerFloorType.setDynamicStatus(DynamicStatus.PASSIVE);
        trailerFloorTypeRepository.save(trailerFloorType);
    }

    private List<TrailerFloorType> engNullCheck(List<TrailerFloorType> trailerFloorTypeList) {
        trailerFloorTypeList.stream().filter(trailerFloorType -> trailerFloorType.getEngTypeName() == null).forEach(trailerFloorType -> trailerFloorType.setEngTypeName(trailerFloorType.getTypeName()));
        return trailerFloorTypeList;
    }

    @Override
    public List<TrailerFloorTypeDto> getActive() {
        return trailerFloorTypeMapper.entityListToDtoList(engNullCheck(trailerFloorTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<TrailerFloorTypeDto> getPassive() {
        return trailerFloorTypeMapper.entityListToDtoList(trailerFloorTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


}
