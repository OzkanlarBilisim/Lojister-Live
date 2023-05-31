package com.lojister.controller.advertisement;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.clientadvertisement.*;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.business.abstracts.ClientAdvertisementService;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.model.enums.ClientAdvertisementType;
import com.lojister.model.enums.RegionAdvertisementType;
import com.lojister.service.osrm.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/clientAdvertisement")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class ClientAdvertisementController {

    private final ClientAdvertisementService clientAdvertisementService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientAdvertisementDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientAdvertisementService.getById(id));
    }
    @OnlyAdmin
    @GetMapping()
    public ResponseEntity<List<ClientAdvertisementDto>> getAll() {

        return ResponseEntity.ok(clientAdvertisementService.getAll());
    }
    @PermitAllCustom
    @GetMapping("/simple")
    public ResponseEntity<List<ClientAdvertisementSimpleDto>> getAllButSimple() {
        return ResponseEntity.ok(clientAdvertisementService.getAllButSimple());
    }

    @PermitAllCustom
    @GetMapping("{id}/simple/hide")
    public ClientAdvertisementSimpleDto getByIdButSimpleAndHide(@PathVariable(value = "id") Long id) {
        return clientAdvertisementService.getByIdButSimpleAndHide(id);
    }

    @PermitAllCustom
    @PostMapping("/minimal/hide")
    public List<ClientAdvertisementMinimalDto> getAdvertisementFilterAndSort(@RequestBody FilterClientAdvertisementRequest filterClientAdvertisementRequest,@RequestParam(defaultValue = "adStartingDate",required = false) String sort) {
        return clientAdvertisementService.getAdvertisementFilterAndSort(filterClientAdvertisementRequest,sort);
    }


    @PermitAllCustom
    @GetMapping("/minimal/hide/startingAddress")
    public List<ClientAdvertisementMinimalDto> getByStartingPositionButMinimalAndHide(@RequestParam Double lat,@RequestParam Double lng) {
        return clientAdvertisementService.getByStartingPositionButMinimalAndHide(lat, lng);
    }

    @PostMapping()
    public ResponseEntity<ClientAdvertisementDto> save(@Valid @RequestBody SaveClientAdvertisementRequest clientAdvertisementDto) {

        return ResponseEntity.ok(clientAdvertisementService.save(clientAdvertisementDto));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        clientAdvertisementService.deleteById(id);
    }


    @GetMapping("/{id}/hide")
    public ResponseEntity<ClientAdvertisementDto> hideAdvertisement(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientAdvertisementService.hideAdvertisement(id));
    }


    @GetMapping("/hidden")
    public ResponseEntity<List<ClientAdvertisementDto>> getHiddenAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getHiddenAdvertisement());
    }


    @GetMapping("/hidden/count")
    public ResponseEntity<Long> getHiddenAdvertisementCount() {

        return ResponseEntity.ok(clientAdvertisementService.getHiddenAdvertisementCount());
    }


    @GetMapping("/{id}/activate")
    public ResponseEntity<ClientAdvertisementDto> activateAdvertisement(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientAdvertisementService.activateAdvertisement(id));
    }


    @GetMapping("/active")
    @PermitAllCustom
    public ResponseEntity<List<ClientAdvertisementDto>> getActiveAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getActiveAdvertisement());
    }


    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveAdvertisementCount() {

        return ResponseEntity.ok(clientAdvertisementService.getActiveAdvertisementCount());
    }


    @GetMapping("/finished")
    public ResponseEntity<List<ClientAdvertisementDto>> getFinishedAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getFinishedAdvertisement());
    }


    @GetMapping("/finished/count")
    public ResponseEntity<Long> getFinishedAdvertisementCount() {

        return ResponseEntity.ok(clientAdvertisementService.getFinishedAdvertisementCount());
    }


    @GetMapping("/transport")
    public ResponseEntity<List<ClientAdvertisementDto>> getTransportAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getTransportAdvertisement());
    }
    @PostMapping("/cargo_on_the_way")
    public List<ClientAdvertisementMinimalDto> getCargoOnTheWayAdvertisement(@RequestBody FilterClientAdvertisementRequest filterClientAdvertisementRequest,@RequestParam(defaultValue = "adStartingDate",required = false) String sort) {
        filterClientAdvertisementRequest.setStartingAddressProvince(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressProvince()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressProvince() : ""
        );
        filterClientAdvertisementRequest.setStartingAddressDistrict(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressDistrict()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressDistrict() : ""
        );
        filterClientAdvertisementRequest.setStartingAddressNeighborhood(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressNeighborhood()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressNeighborhood() : ""
        );
        filterClientAdvertisementRequest.setDueAddressProvince(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressProvince()).isPresent() ? filterClientAdvertisementRequest.getDueAddressProvince() : ""
        );
        filterClientAdvertisementRequest.setDueAddressDistrict(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressDistrict()).isPresent() ? filterClientAdvertisementRequest.getDueAddressDistrict() : ""
        );
        filterClientAdvertisementRequest.setDueAddressNeighborhood(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressNeighborhood()).isPresent() ? filterClientAdvertisementRequest.getDueAddressNeighborhood() : ""
        ); filterClientAdvertisementRequest.setStartingAddressProvince(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressProvince()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressProvince() : ""
        );
        filterClientAdvertisementRequest.setStartingAddressDistrict(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressDistrict()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressDistrict() : ""
        );
        filterClientAdvertisementRequest.setStartingAddressNeighborhood(
                Optional.ofNullable(filterClientAdvertisementRequest.getStartingAddressNeighborhood()).isPresent() ? filterClientAdvertisementRequest.getStartingAddressNeighborhood() : ""
        );
        filterClientAdvertisementRequest.setDueAddressProvince(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressProvince()).isPresent() ? filterClientAdvertisementRequest.getDueAddressProvince() : ""
        );
        filterClientAdvertisementRequest.setDueAddressDistrict(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressDistrict()).isPresent() ? filterClientAdvertisementRequest.getDueAddressDistrict() : ""
        );
        filterClientAdvertisementRequest.setDueAddressNeighborhood(
                Optional.ofNullable(filterClientAdvertisementRequest.getDueAddressNeighborhood()).isPresent() ? filterClientAdvertisementRequest.getDueAddressNeighborhood() : ""
        );
        return clientAdvertisementService.getCargoOnTheWayAdvertisement(filterClientAdvertisementRequest,sort);
    }


    @GetMapping("/myAdvertisement")
    public ResponseEntity<List<ClientAdvertisementDto>> getMyAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getMyAdvertisement());
    }
    @GetMapping("/myAdvertisement/status/{status}")
    public Page<ClientAdvertisementMinimalDto> getByAdvertisementStatus(@PathVariable(value = "status") Boolean status, Pageable pageable) {
        return clientAdvertisementService.getByClientIdAndAdvertisementStatusIn(status, pageable);
    }

    @GetMapping("/myAdvertisement/history")
    public ResponseEntity<List<ClientAdvertisementDto>> getMyAdvertisementByProvincesAndDateRange(@RequestParam("startProvince") String startProvince, @RequestParam("finishProvince") String finishProvince, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        return ResponseEntity.ok(clientAdvertisementService.getMyAdvertisementByProvincesAndDateRange(startProvince, finishProvince, startDate, endDate));
    }


    @GetMapping("/myAdvertisement/count")
    public ResponseEntity<Long> getMyAdvertisementCount() {

        return ResponseEntity.ok(clientAdvertisementService.getMyActiveAdvertisementCount());
    }


    @GetMapping("/partialAdvertisement")
    public ResponseEntity<List<ClientAdvertisementDto>> getPartialAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getPartialAdvertisement());
    }


    @GetMapping("/ftlAdvertisement")
    public ResponseEntity<List<ClientAdvertisementDto>> getFTLAdvertisement() {

        return ResponseEntity.ok(clientAdvertisementService.getFTLAdvertisement());
    }


    @GetMapping("/vehicleTypeIdList")
    public ResponseEntity<List<ClientAdvertisementDto>> getByVehicleTypeIdList(@RequestParam List<Long> values) {

        return ResponseEntity.ok(clientAdvertisementService.getByVehicleTypeIdList(values));
    }


    @GetMapping("/trailerTypeIdList")
    public ResponseEntity<List<ClientAdvertisementDto>> getByTrailerTypeIdList(@RequestParam List<Long> values) {

        return ResponseEntity.ok(clientAdvertisementService.getByTrailerTypeIdList(values));
    }


    @GetMapping("/trailerFloorTypeIdList")
    public ResponseEntity<List<ClientAdvertisementDto>> getByTrailerFloorTypeIdList(@RequestParam List<Long> values) {

        return ResponseEntity.ok(clientAdvertisementService.getByTrailerFloorTypeIdList(values));
    }


    @GetMapping("/trailerFeatureIdList")
    public ResponseEntity<List<ClientAdvertisementDto>> getByTrailerFeatureIdList(@RequestParam List<Long> values) {

        return ResponseEntity.ok(clientAdvertisementService.getByTrailerFeatureIdList(values));
    }


    @GetMapping("/provincesAndDateRange")
    @PermitAllCustom
    public ResponseEntity<List<ClientAdvertisementDto>> getByProvincesAndDateRange(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("startProvince") String startProvince, @RequestParam("dueProvince") String dueProvince) {

        return ResponseEntity.ok(clientAdvertisementService.getByProvincesAndDateRange(startDate, endDate, startProvince, dueProvince));
    }

    @GetMapping("/{id}/myBids")
    public ResponseEntity<List<ClientAdvertisementBidDto>> getClientAdvertisementBidsByClientAdvertisementId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(clientAdvertisementService.getClientAdvertisementBidsByClientAdvertisementId(id));
    }



    //TODO YAPILACAK.
    @PostMapping("/excel")
    public ResponseEntity<List<SaveAdvertisementExcelRequest>> saveByUploadingExcelFile(FileUploadUtil.FileResult result) {

        return ResponseEntity.ok(clientAdvertisementService.uploadFile(result));

    }


}


