package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.LoadTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.LoadTypeMapper;
import com.lojister.model.entity.adminpanel.LoadType;
import com.lojister.repository.advertisement.LoadTypeRepository;
import com.lojister.business.abstracts.dynamic.LoadTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class LoadTypeServiceImpl implements LoadTypeService {

    private final LoadTypeRepository loadTypeRepository;
    private final LoadTypeMapper loadTypeMapper;
    private static final String LOAD_TYPE_NOTFOUND_MESSAGE = "Yükleme Tipi Bulunamadı.";

    @Autowired
    public LoadTypeServiceImpl(LoadTypeRepository loadTypeRepository, LoadTypeMapper loadTypeMapper) {
        this.loadTypeRepository = loadTypeRepository;
        this.loadTypeMapper = loadTypeMapper;
    }

    @Override
    public Set<LoadType> findSetLoadTypeByIdList(List<Long> idList) {

        Set<LoadType> loadTypes = loadTypeRepository.findByIdIn(idList);
        if (!(loadTypes.isEmpty())) {
            return loadTypes;
        } else {
            throw new EntityNotFoundException(LOAD_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<LoadType> findSetLoadTypeByTypeNameList(List<String> typeNameList) {

        Set<LoadType> loadTypes = loadTypeRepository.findByTypeNameInIgnoreCase(typeNameList);
        if (!(loadTypes.isEmpty())) {
            return loadTypes;
        } else {
            throw new EntityNotFoundException(LOAD_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<LoadType> loadType = loadTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (loadType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Yükleme Tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public LoadType findDataById(Long id) {

        Optional<LoadType> loadType = loadTypeRepository.findById(id);

        if (loadType.isPresent()) {
            return loadType.get();

        } else {
            throw new EntityNotFoundException(LOAD_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<LoadType> findAll() {
        return loadTypeRepository.getAll(DynamicStatus.ACTIVE);
    }


    @Override
    @OnlyAdmin
    public LoadTypeDto save(LoadTypeDto loadTypeDto) {

        duplicateTypeNameCheck(loadTypeDto.getTypeName());

        LoadType loadType = new LoadType();
        loadType.setTypeName(loadTypeDto.getTypeName());
        loadType.setEngTypeName(loadTypeDto.getEngTypeName());
        loadType.setDynamicStatus(DynamicStatus.ACTIVE);

        loadType = loadTypeRepository.save(loadType);

        return loadTypeMapper.entityToDto(loadType);
    }


    @Override
    @OnlyAdmin
    public LoadTypeDto update(Long id, LoadTypeDto loadTypeDto) {

        LoadType loadType = findDataById(id);

        loadType.setTypeName(loadTypeDto.getTypeName());
        loadType.setEngTypeName(loadTypeDto.getEngTypeName());
        return loadTypeMapper.entityToDto(loadTypeRepository.save(loadType));
    }


    @Override
    public LoadTypeDto getById(Long id) {

        LoadType loadType = findDataById(id);
        return loadTypeMapper.entityToDto(loadType);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        loadTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<LoadTypeDto> getAll() {
        return loadTypeMapper.entityListToDtoList(loadTypeRepository.findAll());
    }

    @Override
    @OnlyAdmin
    public void activate(Long id) {

        LoadType loadType = findDataById(id);

        loadType.setDynamicStatus(DynamicStatus.ACTIVE);
        loadTypeRepository.save(loadType);
    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        LoadType loadType = findDataById(id);

        loadType.setDynamicStatus(DynamicStatus.PASSIVE);
        loadTypeRepository.save(loadType);
    }

    private List<LoadType> engNullCheck(List<LoadType> loadTypeList) {
        loadTypeList.stream().filter(loadType -> loadType.getEngTypeName() == null).forEach(loadType -> loadType.setEngTypeName(loadType.getTypeName()));
        return loadTypeList;
    }

    @Override
    public List<LoadTypeDto> getActive() {
        return loadTypeMapper.entityListToDtoList(engNullCheck(loadTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<LoadTypeDto> getPassive() {
        return loadTypeMapper.entityListToDtoList(loadTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }
}
