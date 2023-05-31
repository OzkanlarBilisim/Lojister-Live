package com.lojister.controller.waybill;

import com.lojister.core.util.annotation.Authenticated;
import com.lojister.model.dto.SetWaybillStatusDto;
import com.lojister.model.dto.WaybillBase64Dto;
import com.lojister.model.dto.WaybillWithoutDataDto;
import com.lojister.business.abstracts.WaybillService;
import com.lojister.core.api.ApiPaths;
import com.lojister.core.util.FileUploadUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/waybill")
@CrossOrigin
@Authenticated
public class WayBillController {

    private final WaybillService waybillService;

    public WayBillController(WaybillService waybillService) {
        this.waybillService = waybillService;
    }


    @GetMapping("/waiting")
    @ApiOperation(value = "Admin panelde bekleme durumunda olan bütün irsaliye belgelerini çekmek için kullanılacak istek.(Belgelerin datası yoktur.)")
    public ResponseEntity<List<WaybillWithoutDataDto>> getAllWaitingWithoutData() {
        return ResponseEntity.ok(waybillService.getAllWaitingWithoutData());
    }


    @PostMapping("/setStatus")
    @ApiOperation(value = "Admin panelde irsaliye fotoğrafını onaylamak ya da reddetmek için kullanılacak istek.")
    public void setWaybillStatus(@Valid @RequestBody SetWaybillStatusDto waybillStatusDto) {

        waybillService.setWaybillStatus(waybillStatusDto);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "upload waybill file.  'filename' değil 'result' adıyla formdata olarak yollanacak. ")
    public void saveWaybill(MultipartFile result, @RequestParam("transportProcessId") Long transportProcessId) {
        waybillService.saveWaybill(result, transportProcessId);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "İrsaliye belgesi id'sine göre belgenin base64 halini döndüren istek.")
    public ResponseEntity<WaybillBase64Dto> getBase64ById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(waybillService.findBase64ById(id));
    }

    @GetMapping("/changeDeniedStatus")
    public void changeDeniedStatus(@RequestParam(name = "transportCode") String transportCode){
        waybillService.waybillDeniedChangeStatus(transportCode);
    }


}
