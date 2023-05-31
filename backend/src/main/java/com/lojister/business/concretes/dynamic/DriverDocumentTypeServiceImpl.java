package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.DriverDocumentTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.DriverDocumentTypeMapper;
import com.lojister.model.entity.adminpanel.DriverDocumentType;
import com.lojister.repository.driver.DriverDocumentTypeRepository;
import com.lojister.business.abstracts.dynamic.DriverDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DriverDocumentTypeServiceImpl implements DriverDocumentTypeService {

    private final DriverDocumentTypeRepository driverDocumentTypeRepository;
    private final DriverDocumentTypeMapper driverDocumentTypeMapper;


    @Autowired
    public DriverDocumentTypeServiceImpl(DriverDocumentTypeRepository driverDocumentTypeRepository,
                                         DriverDocumentTypeMapper driverDocumentTypeMapper) {
        this.driverDocumentTypeRepository = driverDocumentTypeRepository;
        this.driverDocumentTypeMapper = driverDocumentTypeMapper;
    }

    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<DriverDocumentType> driverDocumentType = driverDocumentTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (driverDocumentType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Sürücü Doküman tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public DriverDocumentType findDataById(Long id) {

        Optional<DriverDocumentType> driverDocumentTypeOptional = driverDocumentTypeRepository.findById(id);

        if (driverDocumentTypeOptional.isPresent()) {

            return driverDocumentTypeOptional.get();
        } else {
            throw new EntityNotFoundException("Sürücü Doküman Tipi Bulunamadı.");
        }
    }


    @Override
    @OnlyAdmin
    public DriverDocumentTypeDto save(DriverDocumentTypeDto driverDocumentTypeDto) {

        duplicateTypeNameCheck(driverDocumentTypeDto.getTypeName());
        return driverDocumentTypeMapper.entityToDto(driverDocumentTypeRepository.save(DriverDocumentType.create(driverDocumentTypeDto.getTypeName(), driverDocumentTypeDto.getEngTypeName())));
    }

    @Override
    public DriverDocumentTypeDto getById(Long id) {
        DriverDocumentType driverDocumentType = findDataById(id);
        return driverDocumentTypeMapper.entityToDto(driverDocumentType);
    }


    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        driverDocumentTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<DriverDocumentTypeDto> getAll() {
        return driverDocumentTypeMapper.entityListToDtoList(driverDocumentTypeRepository.findAll());
    }

    @Override
    @OnlyAdmin
    public void activate(Long id) {

        DriverDocumentType driverDocumentType = findDataById(id);

        driverDocumentType.setDynamicStatus(DynamicStatus.ACTIVE);
        driverDocumentTypeRepository.save(driverDocumentType);

    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        DriverDocumentType driverDocumentType = findDataById(id);

        driverDocumentType.setDynamicStatus(DynamicStatus.PASSIVE);
        driverDocumentTypeRepository.save(driverDocumentType);
    }

    private List<DriverDocumentType> engNullCheck(List<DriverDocumentType> driverDocumentTypeList) {
        driverDocumentTypeList.stream().filter(driverDocumentType -> driverDocumentType.getEngTypeName() == null).forEach(driverDocumentType -> driverDocumentType.setEngTypeName(driverDocumentType.getTypeName()));
        return driverDocumentTypeList;
    }

    @Override
    public List<DriverDocumentTypeDto> getActive() {
        return driverDocumentTypeMapper.entityListToDtoList(engNullCheck(driverDocumentTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<DriverDocumentTypeDto> getPassive() {
        return driverDocumentTypeMapper.entityListToDtoList(driverDocumentTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }
}
