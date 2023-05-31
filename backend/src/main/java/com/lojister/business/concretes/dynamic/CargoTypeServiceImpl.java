package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.CargoTypeDto;
import com.lojister.model.enums.DynamicStatus;
import com.lojister.core.exception.DuplicateAdminPanelEntityException;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.CargoTypeMapper;
import com.lojister.model.entity.adminpanel.CargoType;
import com.lojister.repository.advertisement.CargoTypeRepository;
import com.lojister.business.abstracts.dynamic.CargoTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CargoTypeServiceImpl implements CargoTypeService {

    private final CargoTypeRepository cargoTypeRepository;
    private final CargoTypeMapper cargoTypeMapper;

    private static final String CARGO_TYPE_NOTFOUND_MESSAGE = "Yük Tipi Bulunamadı.";


    @Autowired
    public CargoTypeServiceImpl(CargoTypeRepository cargoTypeRepository,
                                CargoTypeMapper cargoTypeMapper) {
        this.cargoTypeRepository = cargoTypeRepository;
        this.cargoTypeMapper = cargoTypeMapper;
    }


    @Override
    public Set<CargoType> findSetCargoTypesByIdList(List<Long> idList) {

        Set<CargoType> cargoTypeSet = cargoTypeRepository.findByIdIn(idList);

        if (!(cargoTypeSet.isEmpty())) {
            return cargoTypeSet;
        } else {
            throw new EntityNotFoundException(CARGO_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<CargoType> findSetCargoTypesByNameList(List<String> typeNameList) {

        Set<CargoType> cargoTypeSet = cargoTypeRepository.findByTypeNameInIgnoreCase(typeNameList);

        if (!(cargoTypeSet.isEmpty())) {
            return cargoTypeSet;
        } else {
            throw new EntityNotFoundException(CARGO_TYPE_NOTFOUND_MESSAGE);
        }
    }


    @Override
    public void duplicateTypeNameCheck(String typeName) {
        Optional<CargoType> cargoType = cargoTypeRepository.findByTypeNameIgnoreCase(typeName);
        if (cargoType.isPresent()) {
            throw new DuplicateAdminPanelEntityException("Yük Tipi Zaten Mevcuttur.");
        }
    }


    @Override
    @OnlyAdmin
    public CargoTypeDto save(CargoTypeDto cargoTypeDto) {

        duplicateTypeNameCheck(cargoTypeDto.getTypeName());
        return cargoTypeMapper.entityToDto(cargoTypeRepository.save(CargoType.create(cargoTypeDto.getTypeName(), cargoTypeDto.getEngTypeName())));
    }

    @Override
    public CargoTypeDto update(Long id, CargoTypeDto cargoTypeDto) {

        CargoType cargoType = findDataById(id);

        cargoType.setTypeName(cargoTypeDto.getTypeName());
        cargoType.setEngTypeName(cargoTypeDto.getEngTypeName());
        return cargoTypeMapper.entityToDto(cargoTypeRepository.save(cargoType));
    }

    @Override
    public CargoTypeDto getById(Long id) {

        CargoType cargoType = findDataById(id);
        return cargoTypeMapper.entityToDto(cargoType);

    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);
        cargoTypeRepository.deleteById(id);
    }

    @Override
    @OnlyAdmin
    public List<CargoTypeDto> getAll() {

        return cargoTypeMapper.entityListToDtoList(cargoTypeRepository.findAll());
    }

    @Override
    @OnlyAdmin
    public void activate(Long id) {

        CargoType cargoType = findDataById(id);

        cargoType.setDynamicStatus(DynamicStatus.ACTIVE);
        cargoTypeRepository.save(cargoType);
    }


    @Override
    @OnlyAdmin
    public void hide(Long id) {

        CargoType cargoType = findDataById(id);

        cargoType.setDynamicStatus(DynamicStatus.PASSIVE);
        cargoTypeRepository.save(cargoType);
    }

    private List<CargoType> engNullCheck(List<CargoType> cargoTypeList) {
        cargoTypeList.stream().filter(cargoType -> cargoType.getEngTypeName() == null).forEach(cargoType -> cargoType.setEngTypeName(cargoType.getTypeName()));
        return cargoTypeList;
    }

    @Override
    public List<CargoTypeDto> getActive() {
        return cargoTypeMapper.entityListToDtoList(engNullCheck(cargoTypeRepository.findByDynamicStatus(DynamicStatus.ACTIVE)));
    }

    @Override
    public List<CargoTypeDto> getPassive() {
        return cargoTypeMapper.entityListToDtoList(cargoTypeRepository.findByDynamicStatus(DynamicStatus.PASSIVE));
    }


    @Override
    public CargoType findDataById(Long id) {

        Optional<CargoType> cargoType = cargoTypeRepository.findById(id);
        if (cargoType.isPresent()) {
            return cargoType.get();
        } else {
            throw new EntityNotFoundException(CARGO_TYPE_NOTFOUND_MESSAGE);
        }
    }

    @Override
    public Set<CargoType> findAll() {
        return cargoTypeRepository.getAll(DynamicStatus.ACTIVE);
    }


}
