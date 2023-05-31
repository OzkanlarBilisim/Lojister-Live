package com.lojister.business.abstracts;

import com.lojister.model.dto.SavedAddressDto;
import com.lojister.model.entity.SavedAddress;

import java.util.List;

public interface SavedAddressService extends BaseService<SavedAddressDto> {

    List<SavedAddressDto> myAddresses();
   SavedAddress findByIsDefaultAddress(Boolean isDefaultAddress);

   List<SavedAddressDto> updateDefaultAddress(Long id);
   SavedAddress findById(Long id);

    void disableDefaultAddress();
}
