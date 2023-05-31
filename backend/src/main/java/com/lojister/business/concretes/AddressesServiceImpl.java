package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.addresses.AddressesDto;
import com.lojister.model.dto.addresses.DistrictDto;
import com.lojister.model.dto.addresses.NeighborhoodDto;
import com.lojister.model.dto.addresses.ProvinceDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.model.entity.adresses.District;
import com.lojister.model.entity.adresses.Neighborhood;
import com.lojister.model.entity.adresses.Province;
import com.lojister.repository.address.DistrictRepository;
import com.lojister.repository.address.NeighborhoodRepository;
import com.lojister.repository.address.ProvinceRepository;
import com.lojister.business.abstracts.AddressesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressesServiceImpl implements AddressesService {

    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final NeighborhoodRepository neighborhoodRepository;



    @Override
    public Province getProvinceById(Long id) {

        Optional<Province> provinceOptional = provinceRepository.findById(id);

        if (provinceOptional.isPresent()) {
            return provinceOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.address.EntityNotFoundException.city"));
        }

    }

    @Override
    public List<ProvinceDto> getAllProvince() {
        return provinceRepository.findAllProvinceDto();
    }

    @Override
    public District getDistrictById(Long id) {

        Optional<District> districtOptional = districtRepository.findById(id);

        if (districtOptional.isPresent()) {
            return districtOptional.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.address.EntityNotFoundException.district"));
        }

    }

    @Override
    public List<DistrictDto> getAllDistrictByProvinceId(Long provinceId) {
        return districtRepository.findByProvinceIdDto(provinceId);
    }

    @Override
    public List<DistrictDto> getAllDistrictByProvinceName(String provinceName) {
        return districtRepository.findByProvinceNameDto(provinceName);
    }


    @Override
    public Neighborhood getByNeighborhoodId(Long neighborhoodId) {

        Optional<Neighborhood> optionalNeighborhood = neighborhoodRepository.findById(neighborhoodId);

        if (optionalNeighborhood.isPresent()) {
            return optionalNeighborhood.get();
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.address.EntityNotFoundException.neigborhood"));
        }
    }

    @Override
    public List<NeighborhoodDto> getAllNeighborhoodByDistrictId(Long districtId) {
        return neighborhoodRepository.findByDistrictIdDto(districtId);
    }

    @Override
    public List<NeighborhoodDto> getAllNeighborhoodByProvinceNameAndDistrictName(String provinceName, String districtName) {
        return neighborhoodRepository.findByProvinceNameAndDistrictNameDto(provinceName, districtName);
    }

    public Province findByProvinceName(String provinceName) {

        Optional<Province> province = provinceRepository.findByProvinceNameIgnoreCase(provinceName);

        if (!province.isPresent()) {
            throw new EntityNotFoundException(Translator.toLocale("lojister.address.EntityNotFoundException.city"));
        }
        return province.get();

    }

    @Override
    public AddressesDto findByWord(String word) {

        AddressesDto addressesDto = new AddressesDto();
        addressesDto.setProvince(provinceRepository.findByProvinceNameContains(word));
        addressesDto.setDistrict(districtRepository.findByDistrictNameContains(word));
        addressesDto.setNeighborhood(neighborhoodRepository.findByNeighborhoodNameContains(word));

        return addressesDto;
    }
}
