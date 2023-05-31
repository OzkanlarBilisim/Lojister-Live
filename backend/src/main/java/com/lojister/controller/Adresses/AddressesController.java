package com.lojister.controller.Adresses;

import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.GeoCodeAddressDto;
import com.lojister.model.dto.PositionDto;
import com.lojister.model.dto.addresses.AddressesDto;
import com.lojister.model.dto.addresses.DistrictDto;
import com.lojister.model.dto.addresses.NeighborhoodDto;
import com.lojister.model.dto.addresses.ProvinceDto;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.adresses.District;
import com.lojister.model.entity.adresses.Neighborhood;
import com.lojister.model.entity.adresses.Province;
import com.lojister.business.abstracts.AddressesService;
import com.lojister.service.gmaps.GMapsService;
import com.lojister.core.api.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@CrossOrigin
@PermitAllCustom
public class AddressesController {


    //TODO permitallcustom

    private final AddressesService addressesService;
    private final GMapsService gMapsService;

    @Autowired
    public AddressesController(AddressesService addressesService, GMapsService gMapsService) {
        this.addressesService = addressesService;
        this.gMapsService = gMapsService;
    }

    @GetMapping("/province/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(addressesService.getProvinceById(id));
    }

    @GetMapping("/province")
    public ResponseEntity<List<ProvinceDto>> getAllProvince() {

        return ResponseEntity.ok(addressesService.getAllProvince());
    }

    @GetMapping("/district/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(addressesService.getDistrictById(id));
    }

    @GetMapping("/province/{provinceId}/district")
    public ResponseEntity<List<DistrictDto>> getAllDistrictByProvinceId(@PathVariable(name = "provinceId") Long provinceId) {

        return ResponseEntity.ok(addressesService.getAllDistrictByProvinceId(provinceId));
    }

    @GetMapping("/district")
    public ResponseEntity<List<DistrictDto>> getAllDistrictByProvinceName(@RequestParam(name = "provinceName") String provinceName) {

        return ResponseEntity.ok(addressesService.getAllDistrictByProvinceName(provinceName));
    }


    @GetMapping("/neighborhood/{id}")
    public ResponseEntity<Neighborhood> getByNeighborhoodId(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(addressesService.getByNeighborhoodId(id));
    }

    @GetMapping("/district/{districtId}/neighborhood")
    public ResponseEntity<List<NeighborhoodDto>> getAllNeighborhoodByDistrictId(@PathVariable(name = "districtId") Long districtId) {

        return ResponseEntity.ok(addressesService.getAllNeighborhoodByDistrictId(districtId));
    }

    @GetMapping("/neighborhood")
    public ResponseEntity<List<NeighborhoodDto>> getAllNeighborhoodByProvinceNameAndDistrictName(@RequestParam(name = "provinceName") String provinceName, @RequestParam(name = "districtName") String districtName) {

        return ResponseEntity.ok(addressesService.getAllNeighborhoodByProvinceNameAndDistrictName(provinceName, districtName));
    }

    @GetMapping("/word")
    public ResponseEntity<AddressesDto> getByWord(@RequestParam(value = "word") String word) {

        return ResponseEntity.ok(addressesService.findByWord(word));
    }


    @PostMapping("/geocode")
    public ResponseEntity<PositionDto> geocodeFromAddressObject(@RequestBody GeoCodeAddressDto address) {

        return ResponseEntity.ok(gMapsService.geocodeFromAddress(address));
    }

    @PostMapping("/reverseGeocode")
    public ResponseEntity<AdvertisementAddress> reverseGeocode(@RequestBody PositionDto position) {
        return ResponseEntity.ok(gMapsService.reverseGeocode(position));

    }


}
