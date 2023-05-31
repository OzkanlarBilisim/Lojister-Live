package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.model.dto.SavedAddressDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.mapper.SavedAddressMapper;
import com.lojister.model.entity.SavedAddress;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.Client;
import com.lojister.repository.address.SavedAddressRepository;
import com.lojister.business.abstracts.SavedAddressService;
import com.lojister.core.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SavedAddressServiceImpl implements SavedAddressService {

    private final SavedAddressRepository savedAddressRepository;
    private final SavedAddressMapper savedAddressMapper;
    private final SecurityContextUtil securityContextUtil;
    private static final String SAVED_ADDRESS_NOTFOUND_MESSAGE = "Kaydedilen adres bulunamadı.";
    private static final String SAVED_ADDRESS_NOTFOUND_MESSAGE_EN = "The registered address could not be found.";



    @Override
    public SavedAddressDto save(SavedAddressDto savedAddressDto) {

        SavedAddress savedAddress = new SavedAddress();

        User currentUser = securityContextUtil.getCurrentUser();

        savedAddress.setAddressName(savedAddressDto.getAddressName());
        savedAddress.setProvince(savedAddressDto.getProvince());
        savedAddress.setDistrict(savedAddressDto.getDistrict());
        savedAddress.setNeighborhood(savedAddressDto.getNeighborhood());
        savedAddress.setCountry(savedAddressDto.getCountry());
        savedAddress.setZipCode(savedAddressDto.getZipCode());
        savedAddress.setFullAddress(savedAddressDto.getFullAddress());
        savedAddress.setLat(savedAddressDto.getLat());
        savedAddress.setLng(savedAddressDto.getLng());
        savedAddress.setCommercialTitle(savedAddressDto.getCommercialTitle());
        savedAddress.setFirstName(savedAddressDto.getFirstName());
        savedAddress.setLastName(savedAddressDto.getLastName());
        savedAddress.setPhone(savedAddressDto.getPhone());
        savedAddress.setUser(currentUser);
        savedAddress.setRegionType(savedAddressDto.getRegionType());
        savedAddress = savedAddressRepository.save(savedAddress);

        return savedAddressMapper.entityToDto(savedAddress);
    }

    @Override
    public SavedAddressDto update(Long id, SavedAddressDto savedAddressDto) {

        Optional<SavedAddress> savedAddress = savedAddressRepository.findById(id);

        if (savedAddress.isPresent()) {
            if (savedAddress.get().getUser().getId().equals(securityContextUtil.getCurrentUserId()) ) {
                savedAddress.get().setAddressName(savedAddressDto.getAddressName());
                savedAddress.get().setProvince(savedAddressDto.getProvince());
                savedAddress.get().setDistrict(savedAddressDto.getDistrict());
                savedAddress.get().setNeighborhood(savedAddressDto.getNeighborhood());
                savedAddress.get().setFullAddress(savedAddressDto.getFullAddress());
                savedAddress.get().setLat(savedAddressDto.getLat());
                savedAddress.get().setLng(savedAddressDto.getLng());
                savedAddress.get().setCommercialTitle(savedAddressDto.getCommercialTitle());
                savedAddress.get().setFirstName(savedAddressDto.getFirstName());
                savedAddress.get().setLastName(savedAddressDto.getLastName());
                savedAddress.get().setPhone(savedAddressDto.getPhone());

                return savedAddressMapper.entityToDto(savedAddressRepository.save(savedAddress.get()));

            } else {
                throw new UnauthorizedTransactionException(Translator.toLocale("lojister.savedAddress.UnauthorizedTransactionException"));
            }
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.savedAddress.EntityNotFoundException"));
        }

    }

    @Override
    public SavedAddressDto getById(Long id) {

        Optional<SavedAddress> savedAddress = savedAddressRepository.findById(id);

        if (savedAddress.isPresent()) {
            return savedAddressMapper.entityToDto(savedAddress.get());
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.savedAddress.EntityNotFoundException"));
        }
    }

    @Override
    public void deleteById(Long id) {

        Optional<SavedAddress> savedAddress = savedAddressRepository.findById(id);

        if (savedAddress.isPresent()) {

            if (savedAddress.get().getUser().getId() != securityContextUtil.getCurrentUserId()) {

                throw new UnauthorizedTransactionException(Translator.toLocale("lojister.savedAddress.UnauthorizedTransactionException"));
            }

            savedAddressRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.savedAddress.EntityNotFoundException"));
        }
    }

    @Deprecated
    @Override
    public List<SavedAddressDto> getAll() {
        return Collections.emptyList();
    }


    @Override
    public List<SavedAddressDto> myAddresses() {

        return savedAddressMapper.entityListToDtoList(savedAddressRepository.findByUser_Id(securityContextUtil.getCurrentUserId()));

    }

    @Override
    public SavedAddress findByIsDefaultAddress(Boolean isDefaultAddress) {
        Client client=securityContextUtil.getCurrentClient();
        return savedAddressRepository.findByUser_IdAndIsDefaultAddress(client.getId(),isDefaultAddress).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<SavedAddressDto> updateDefaultAddress(Long id) {
        Client client=securityContextUtil.getCurrentClient();
        List<SavedAddress> savedAddressList=savedAddressRepository.findByUser_Id(client.getId());
        savedAddressList.stream().forEach((savedAddress)->{
            if (savedAddress.getIsDefaultAddress()==Boolean.TRUE||savedAddress.getIsDefaultAddress()==null){
                savedAddress.setIsDefaultAddress(Boolean.FALSE);
                savedAddressRepository.save(savedAddress);
            }
        });
        SavedAddress savedAddress=findById(id);
        savedAddress.setIsDefaultAddress(Boolean.TRUE);
        savedAddressRepository.save(savedAddress);
        return myAddresses();
    }

    @Override
    public SavedAddress findById(Long id) {
        return savedAddressRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void disableDefaultAddress() {
        Client client=securityContextUtil.getCurrentClient();
        List<SavedAddress> savedAddressList=savedAddressRepository.findByUser_Id(client.getId());
        savedAddressList.stream().forEach((savedAddress)->{
                savedAddress.setIsDefaultAddress(Boolean.FALSE);
                savedAddressRepository.save(savedAddress);
        });
        savedAddressRepository.saveAll(savedAddressList);

    }
}
