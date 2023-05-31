package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.VehicleDocumentTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.VehicleDocumentTypeMapper;
import com.lojister.model.entity.adminpanel.VehicleDocumentType;
import com.lojister.repository.vehicle.VehicleDocumentTypeRepository;
import com.lojister.business.abstracts.dynamic.VehicleDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleDocumentTypeServiceImpl implements VehicleDocumentTypeService {

    private final VehicleDocumentTypeRepository vehicleDocumentTypeRepository;
    private final VehicleDocumentTypeMapper vehicleDocumentTypeMapper;

    @Autowired
    public VehicleDocumentTypeServiceImpl(VehicleDocumentTypeRepository vehicleDocumentTypeRepository,
                                          VehicleDocumentTypeMapper vehicleDocumentTypeMapper) {
        this.vehicleDocumentTypeRepository = vehicleDocumentTypeRepository;
        this.vehicleDocumentTypeMapper = vehicleDocumentTypeMapper;
    }


    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<VehicleDocumentType> vehicleDocumentType = vehicleDocumentTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (vehicleDocumentType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Araç Doküman Tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public VehicleDocumentType findDataById(Long id) {

        Optional<VehicleDocumentType> vehicleDocumentType = vehicleDocumentTypeRepository.findById(id);

        if (vehicleDocumentType.isPresent()) {

            return vehicleDocumentType.get();
        } else {
            throw new EntityNotFoundException("Araç Doküman Tipi Bulunamadı.");
        }

    }

    @Override
    @OnlyAdmin
    public VehicleDocumentTypeDto save(VehicleDocumentTypeDto vehicleDocumentTypeDto) {

        duplicateTypeNameCheck(vehicleDocumentTypeDto.getTypeName());
        return vehicleDocumentTypeMapper.entityToDto(vehicleDocumentTypeRepository.save(VehicleDocumentType.create(vehicleDocumentTypeDto.getTypeName(), vehicleDocumentTypeDto.getEngTypeName())));
    }

    @Override
    public VehicleDocumentTypeDto getById(Long id) {

        VehicleDocumentType vehicleDocumentType = findDataById(id);

        return vehicleDocumentTypeMapper.entityToDto(vehicleDocumentType);

    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        vehicleDocumentTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<VehicleDocumentTypeDto> getAll() {
        return vehicleDocumentTypeMapper.entityListToDtoList(vehicleDocumentTypeRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        VehicleDocumentType vehicleDocumentType = findDataById(id);

        vehicleDocumentType.setDynamicStatus(DynamicStatus.ACTIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType);

    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        VehicleDocumentType vehicleDocumentType = findDataById(id);

        vehicleDocumentType.setDynamicStatus(DynamicStatus.PASSIVE);
        vehicleDocumentTypeRepository.save(vehicleDocumentType);
    }

    private List<VehicleDocumentType> engNullCheck(List<VehicleDocumentType> vehicleDocumentTypeList) {
        vehicleDocumentTypeList.stream().filter(vehicleDocumentType -> vehicleDocumentType.getEngTypeName() == null).forEach(vehicleDocumentType -> vehicleDocumentType.setEngTypeName(vehicleDocumentType.getTypeName()));
        return vehicleDocumentTypeList;
    }

    @Override
    public List<VehicleDocumentTypeDto> getActive() {
        return vehicleDocumentTypeMapper.entityListToDtoList(engNullCheck(vehicleDocumentTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<VehicleDocumentTypeDto> getPassive() {
        return vehicleDocumentTypeMapper.entityListToDtoList(vehicleDocumentTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


}
