package com.lojister.controller.advertisement;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidMinimalDto;
import com.lojister.model.dto.clientadvertisementbid.ClientAdvertisementBidSaveDto;
import com.lojister.business.abstracts.ClientAdvertisementBidService;
import com.lojister.core.api.ApiPaths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/clientAdvertisementBid")
@CrossOrigin
@Authenticated
public class ClientAdvertisementBidController {

    private final ClientAdvertisementBidService clientAdvertisementBidService;

    public ClientAdvertisementBidController(ClientAdvertisementBidService clientAdvertisementBidService) {
        this.clientAdvertisementBidService = clientAdvertisementBidService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientAdvertisementBidDto> getById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(clientAdvertisementBidService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<ClientAdvertisementBidDto>> getAll() {
        return ResponseEntity.ok(clientAdvertisementBidService.getAll());
    }

    @PostMapping("/{clientAdvertisementId}")
    public ResponseEntity<ClientAdvertisementBidDto> save(@Valid @RequestBody ClientAdvertisementBidSaveDto clientAdvertisementBidDto, @PathVariable("clientAdvertisementId") Long clientAdvertisementId) {
        return ResponseEntity.ok(clientAdvertisementBidService.save(clientAdvertisementBidDto, clientAdvertisementId));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        clientAdvertisementBidService.deleteById(id);
    }

    @PostMapping("/{advertisementBidId}/updateBidStatus")
    public void updateBidStatus(@PathVariable("advertisementBidId") Long advertisementBidId, @RequestParam Boolean value) {

        clientAdvertisementBidService.updateBidStatus(value, advertisementBidId);
    }

    @GetMapping("/myAdvertisementBids/{advertisementId}")
    public Page<ClientAdvertisementBidDto> getMyAdvertisementBids(@PathVariable("advertisementId") Long advertisementId, Pageable pageable) {

        return clientAdvertisementBidService.getAdvertisementBidsByAdvertisementId(advertisementId,pageable);
    }
    @PostMapping("/myAdvertisementBids/cargoOnTheWay")
    public List<ClientAdvertisementBidDto> getMyAdvertisementBids(@RequestBody FilterClientAdvertisementRequest filterClientAdvertisementRequest,@RequestParam(defaultValue = "createdDateTime",required = false) String sort) {
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
        );
        return clientAdvertisementBidService.getAdvertisementBidsByCargoOnTheWay(filterClientAdvertisementRequest,sort);
    }

    @GetMapping("/getMyBids/{status}")
    public Page<ClientAdvertisementBidMinimalDto> getMyBids(@PathVariable(value = "status") Boolean status, Pageable pageable) {

        return clientAdvertisementBidService.getMyBids(status,pageable);

    }

    @GetMapping("/myAdvertisementBids/count")
    public ResponseEntity<Long> getMyAdvertisementBidsCount() {

        return ResponseEntity.ok(clientAdvertisementBidService.myAdvertisementBidsCount());
    }

    @GetMapping("/myBids/count")
    public ResponseEntity<Long> getMyBidsCount() {

        return ResponseEntity.ok(clientAdvertisementBidService.myBidsCount());
    }


}
