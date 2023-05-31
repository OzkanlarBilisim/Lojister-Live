package com.lojister.controller.client;

import com.lojister.controller.advertisement.SaveClientAdvertisementRequest;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.SavedAddressDto;
import com.lojister.model.dto.client.*;
import com.lojister.model.dto.request.ChangePasswordDto;
import com.lojister.business.abstracts.ClientService;
import com.lojister.model.entity.AdvertisementAddress;
import com.lojister.model.entity.SavedAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin
@Authenticated
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable(value = "id") Long id) {

        return ResponseEntity.ok(clientService.getById(id));
    }

    @GetMapping()
    public ResponseEntity<List<ClientDto>> getAll() {

        return ResponseEntity.ok(clientService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientUpdateResponseDto> update(@PathVariable(name = "id") Long id, @RequestBody ClientUpdateRequestDto clientDto) {

        return ResponseEntity.ok(clientService.update(id, clientDto));
    }

    @PostMapping("/changePassword")
    public Boolean changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return clientService.changePassword(changePasswordDto.getOldPassword(), changePasswordDto.getNwPassword());
    }

    @GetMapping("/dashboardInformation")
    public ClientDashboardResponse getDashboardInformation(){
     return clientService.getDashboardInformation();
    }


    @PutMapping("/updateNotificationSetting")
    public ClientNotificationSettingDto updateNotificationSetting(@RequestBody UpdateClientNotificationSettingRequest updateClientNotificationSettingRequest){
       return clientService.updateNotificationSetting(updateClientNotificationSettingRequest);
    }
    @GetMapping("/getNotificationSetting")
    public ClientNotificationSettingDto getNotificationSetting(){
        return  clientService.getNotificationSetting();
    }

    @PutMapping("/updateAccountSetting")
    public ClientAccountSettingDto updateAccountSetting(@RequestBody UpdateClientAccountSettingRequest updateClientAccountSettingRequest){
        return clientService.updateAccountSetting(updateClientAccountSettingRequest);
    }
    @GetMapping("/getAccountSetting")
    public ClientAccountSettingDto getAccountSetting(){
        return  clientService.getAccountSetting();
    }

    @PostMapping("/employee")
    public ClientDto saveEmployee(@RequestBody @Valid SaveClientEmployeeRequest saveClientEmployeeRequest) {
        return clientService.saveEmployee(saveClientEmployeeRequest);
    }
    @DeleteMapping("/employee/{employeeId}")
    public void deleteEmployee(@PathVariable Long employeeId){
        clientService.deleteEmployee(employeeId);
    }

    @GetMapping("/myEmployee")
    public Page<ClientDto> getEmployee(Pageable pageable){
        return clientService.getMyEmployee(pageable);
    }

    @GetMapping("/myDefaultAddress")
    public SavedAddressDto getMyDefaultAddress(){
        return clientService.getMyDefaultAddress();
    }
}
