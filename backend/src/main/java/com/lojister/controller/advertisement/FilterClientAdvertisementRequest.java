package com.lojister.controller.advertisement;

import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.enums.RegionAdvertisementType;
import lombok.*;
import java.util.List;

@Getter
@Setter
public class FilterClientAdvertisementRequest {
    String startingAddressCountry;
    String startingAddressProvince ;
    String startingAddressDistrict;
    String startingAddressNeighborhood;
    String dueAddressCountry;
    String dueAddressProvince;
    String dueAddressDistrict;
    String dueAddressNeighborhood;
    List<ClientAdvertisementType> clientAdvertisementTypeList;
    List<RegionAdvertisementType> regionAdvertisementTypeList;
}
