package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.PackagingTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.PackagingTypeMapper;
import com.lojister.model.entity.adminpanel.PackagingType;
import com.lojister.repository.advertisement.PackagingTypeRepository;
import com.lojister.business.abstracts.dynamic.PackagingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PackagingTypeServiceImpl implements PackagingTypeService {

    private final PackagingTypeRepository packagingTypeRepository;
    private final PackagingTypeMapper packagingTypeMapper;

    @Autowired
    public PackagingTypeServiceImpl(PackagingTypeRepository packagingTypeRepository,
                                    PackagingTypeMapper packagingTypeMapper) {
        this.packagingTypeRepository = packagingTypeRepository;
        this.packagingTypeMapper = packagingTypeMapper;
    }


    @Override
    public PackagingType findDataById(Long id) {

        Optional<PackagingType> packagingType = packagingTypeRepository.findById(id);

        if (packagingType.isPresent()) {

            return packagingType.get();
        } else {
            throw new EntityNotFoundException("Paketleme Tipi Bulunamadı.");
        }
    }

    @Override
    public PackagingType findDataByTypeName(String typeName) {

        Optional<PackagingType> packagingType = packagingTypeRepository.findByTypeName(typeName);

        if (packagingType.isPresent()) {

            return packagingType.get();
        } else {
            throw new EntityNotFoundException("Paketleme Tipi Bulunamadı.");
        }
    }

    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<PackagingType> packagingType = packagingTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (packagingType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Paketleme Tipi Zaten Mevcuttur.");
        }
    }


    @Override
    @OnlyAdmin
    public PackagingTypeDto save(PackagingTypeDto packagingTypeDto) {

        duplicateTypeNameCheck(packagingTypeDto.getTypeName());
        return packagingTypeMapper.entityToDto(packagingTypeRepository.save(PackagingType.create(packagingTypeDto.getTypeName(), packagingTypeDto.getEngTypeName())));
    }

    @Override
    @OnlyAdmin
    public PackagingTypeDto update(Long id, PackagingTypeDto packagingTypeDto) {

        PackagingType packagingType = findDataById(id);

        packagingType.setTypeName(packagingTypeDto.getTypeName());
        packagingType.setEngTypeName(packagingTypeDto.getEngTypeName());

        return packagingTypeMapper.entityToDto(packagingTypeRepository.save(packagingType));

    }

    @Override
    public PackagingTypeDto getById(Long id) {

        PackagingType packagingType = findDataById(id);

        return packagingTypeMapper.entityToDto(packagingType);

    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);

        packagingTypeRepository.deleteById(id);

    }

    @Override
    @OnlyAdmin
    public List<PackagingTypeDto> getAll() {
        return packagingTypeMapper.entityListToDtoList(packagingTypeRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        PackagingType packagingType = findDataById(id);

        packagingType.setDynamicStatus(DynamicStatus.ACTIVE);
        packagingTypeRepository.save(packagingType);
    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        PackagingType packagingType = findDataById(id);

        packagingType.setDynamicStatus(DynamicStatus.PASSIVE);
        packagingTypeRepository.save(packagingType);
    }

    private List<PackagingType> engNullCheck(List<PackagingType> packagingTypeList) {
        packagingTypeList.stream().filter(packagingType -> packagingType.getEngTypeName() == null).forEach(packagingType -> packagingType.setEngTypeName(packagingType.getTypeName()));
        return packagingTypeList;
    }

    @Override
    public List<PackagingTypeDto> getActive() {
        return packagingTypeMapper.entityListToDtoList(engNullCheck(packagingTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<PackagingTypeDto> getPassive() {
        return packagingTypeMapper.entityListToDtoList(packagingTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


}
