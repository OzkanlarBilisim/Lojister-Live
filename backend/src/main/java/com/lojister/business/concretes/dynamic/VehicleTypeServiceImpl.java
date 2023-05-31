package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.VehicleTypeMapper;
import com.lojister.model.entity.adminpanel.VehicleType;
import com.lojister.repository.vehicle.VehicleTypeRepository;
import com.lojister.business.abstracts.dynamic.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleTypeMapper vehicleTypeMapper;

    private static final String VEHICLE_TYPE_NOTFOUND_MESSAGE = "Araç Tipi Bulunamadı.";

    @Autowired
    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository, VehicleTypeMapper vehicleTypeMapper) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleTypeMapper = vehicleTypeMapper;
    }

    @Override
    public Set<VehicleType> findSetVehicleTypesByIdList(List<Long> idList) {

        if (idList.get(0) == 0L) {

            return new HashSet<>(vehicleTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE));
        }

        Set<VehicleType> vehicleTypeSet = vehicleTypeRepository.findByIdInAndDynamicStatus(idList, DynamicStatus.ACTIVE);

        if (!(vehicleTypeSet.isEmpty())) {

            return vehicleTypeSet;

        } else {
            throw new EntityNotFoundException(VEHICLE_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<VehicleType> findSetVehicleTypesByTypeNameList(List<String> typeNameList) {

        Set<VehicleType> vehicleTypeSet = vehicleTypeRepository.findByTypeNameInIgnoreCase(typeNameList);

        if (!(vehicleTypeSet.isEmpty())) {
            return vehicleTypeSet;
        } else {
            throw new EntityNotFoundException(VEHICLE_TYPE_NOTFOUND_MESSAGE);
        }
    }


    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<VehicleType> vehicleType = vehicleTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (vehicleType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Araç Tipi Zaten Mevcuttur.");
        }
    }

    @Override
    public VehicleType findDataById(Long id) {

        Optional<VehicleType> vehicleType = vehicleTypeRepository.findById(id);

        if (vehicleType.isPresent()) {
            return vehicleType.get();
        } else {
            throw new EntityNotFoundException(VEHICLE_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<VehicleType> findAll() {
        return vehicleTypeRepository.getAll(DynamicStatus.ACTIVE);
    }


    @Override
    @OnlyAdmin
    public VehicleTypeDto save(VehicleTypeDto vehicleTypeDto) {

        duplicateTypeNameCheck(vehicleTypeDto.getTypeName());
        return vehicleTypeMapper.entityToDto(vehicleTypeRepository.save(VehicleType.create(vehicleTypeDto.getTypeName(), vehicleTypeDto.getEngTypeName())));
    }

    @Override
    @OnlyAdmin
    public VehicleTypeDto update(Long id, VehicleTypeDto vehicleTypeDto) {

        VehicleType vehicleType = findDataById(id);

        vehicleType.setTypeName(vehicleTypeDto.getTypeName());
        vehicleType.setEngTypeName(vehicleTypeDto.getEngTypeName());

        return vehicleTypeMapper.entityToDto(vehicleTypeRepository.save(vehicleType));
    }

    @Override
    public VehicleTypeDto getById(Long id) {

        VehicleType vehicleType = findDataById(id);

        return vehicleTypeMapper.entityToDto(vehicleType);
    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);

        vehicleTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<VehicleTypeDto> getAll() {
        return vehicleTypeMapper.entityListToDtoList(vehicleTypeRepository.findAll());
    }


    @Override
    @OnlyAdmin
    public void activate(Long id) {

        VehicleType vehicleType = findDataById(id);

        vehicleType.setDynamicStatus(DynamicStatus.ACTIVE);
        vehicleTypeRepository.save(vehicleType);
    }

    @Override
    @OnlyAdmin
    public void hide(Long id) {

        VehicleType vehicleType = findDataById(id);

        vehicleType.setDynamicStatus(DynamicStatus.PASSIVE);
        vehicleTypeRepository.save(vehicleType);
    }

    private List<VehicleType> engNullCheck(List<VehicleType> vehicleTypeList) {
        vehicleTypeList.stream().filter(vehicleType -> vehicleType.getEngTypeName() == null).forEach(vehicleType -> vehicleType.setEngTypeName(vehicleType.getTypeName()));
        return vehicleTypeList;
    }

    @Override
    public List<VehicleTypeDto> getActive() {
        return vehicleTypeMapper.entityListToDtoList(engNullCheck(vehicleTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<VehicleTypeDto> getPassive() {
        return vehicleTypeMapper.entityListToDtoList(vehicleTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


}
