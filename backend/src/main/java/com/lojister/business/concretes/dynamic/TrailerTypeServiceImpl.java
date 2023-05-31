package com.lojister.business.concretes.dynamic;

import com.lojister.business.abstracts.dynamic.TrailerTypeService;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.TrailerTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.TrailerTypeMapper;
import com.lojister.model.entity.adminpanel.TrailerType;
import com.lojister.repository.trailer.TrailerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TrailerTypeServiceImpl implements TrailerTypeService {

    private final TrailerTypeRepository trailerTypeRepository;
    private final TrailerTypeMapper trailerTypeMapper;

    private static final String TRAILER_TYPE_NOTFOUND_MESSAGE = "Dorse Tipi Bulunamadı.";

    @Autowired
    public TrailerTypeServiceImpl(TrailerTypeRepository trailerTypeRepository,
                                  TrailerTypeMapper trailerTypeMapper) {
        this.trailerTypeRepository = trailerTypeRepository;
        this.trailerTypeMapper = trailerTypeMapper;
    }


    @Override
    public Set<TrailerType> findSetTrailerTypesByIdList(List<Long> idList) {

   /*     if (idList.get(0) == 0L) {

            return new HashSet<>(trailerTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE));
        }*/

        Set<TrailerType> trailerTypeSet = trailerTypeRepository.findByIdInAndDynamicStatus(idList, DynamicStatus.ACTIVE);

        if (!(trailerTypeSet.isEmpty())) {
            return trailerTypeSet;
        } else {
            throw new EntityNotFoundException(TRAILER_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerType> findSetTrailerTypesByTypeNameList(List<String> typeNameList) {

        Set<TrailerType> trailerTypeSet = trailerTypeRepository.findByTypeNameInIgnoreCase(typeNameList);

        if (!(trailerTypeSet.isEmpty())) {
            return trailerTypeSet;
        } else {
            throw new EntityNotFoundException(TRAILER_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<TrailerType> trailerType = trailerTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (trailerType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Dorse Tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public TrailerType findDataById(Long id) {

        Optional<TrailerType> trailerType = trailerTypeRepository.findById(id);

        if (trailerType.isPresent()) {

            return trailerType.get();

        } else {

            throw new EntityNotFoundException(TRAILER_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<TrailerType> findAll() {
        return trailerTypeRepository.getAll(DynamicStatus.ACTIVE);
    }

    @Override
    @OnlyAdmin
    public TrailerTypeDto save(TrailerTypeDto trailerTypeDto) {

        duplicateTypeNameCheck(trailerTypeDto.getTypeName());
        return trailerTypeMapper.entityToDto(trailerTypeRepository.save(TrailerType.create(trailerTypeDto.getTypeName(), trailerTypeDto.getEngTypeName())));
    }

    @Override
    @OnlyAdmin
    public TrailerTypeDto update(Long id, TrailerTypeDto trailerTypeDto) {

        TrailerType trailerType = findDataById(id);

        trailerType.setTypeName(trailerTypeDto.getTypeName());
        trailerType.setEngTypeName(trailerTypeDto.getEngTypeName());
        return trailerTypeMapper.entityToDto(trailerTypeRepository.save(trailerType));

    }

    @Override
    public TrailerTypeDto getById(Long id) {

        TrailerType trailerType = findDataById(id);
        return trailerTypeMapper.entityToDto(trailerType);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        trailerTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<TrailerTypeDto> getAll() {
        return trailerTypeMapper.entityListToDtoList(trailerTypeRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        TrailerType trailerType = findDataById(id);

        trailerType.setDynamicStatus(DynamicStatus.ACTIVE);
        trailerTypeRepository.save(trailerType);

    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        TrailerType trailerType = findDataById(id);

        trailerType.setDynamicStatus(DynamicStatus.PASSIVE);
        trailerTypeRepository.save(trailerType);
    }

    private List<TrailerType> engNullCheck(List<TrailerType> trailerTypeList) {
        trailerTypeList.stream().filter(trailerType -> trailerType.getEngTypeName() == null).forEach(trailerType -> trailerType.setEngTypeName(trailerType.getTypeName()));
        return trailerTypeList;
    }

    @Override
    public List<TrailerTypeDto> getActive() {
        return trailerTypeMapper.entityListToDtoList(engNullCheck(trailerTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<TrailerTypeDto> getPassive() {
        return trailerTypeMapper.entityListToDtoList(trailerTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


}
