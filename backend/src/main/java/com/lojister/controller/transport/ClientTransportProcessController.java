package com.lojister.controller.transport;

import com.lojister.controller.document.SaveDocumentFileRequest;
import com.lojister.core.util.FileUploadUtil;
import com.lojister.core.util.annotation.Authenticated;
import com.lojister.core.util.annotation.PermitAllCustom;
import com.lojister.model.dto.ClientCargoCompletedCheckDto;
import com.lojister.model.dto.clientadvertisement.ClientAdvertisementContainerDto;
import com.lojister.model.dto.clienttransportprocess.ClientTransportProcessDto;
import com.lojister.business.abstracts.ClientTransportProcessService;
import com.lojister.core.api.ApiPaths;
import com.lojister.model.enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
@RequestMapping("/clientTransportProcess")
@CrossOrigin
@Authenticated
public class ClientTransportProcessController {


    private final ClientTransportProcessService clientTransportProcessService;

    @Autowired
    public ClientTransportProcessController(ClientTransportProcessService clientTransportProcessService) {
        this.clientTransportProcessService = clientTransportProcessService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientTransportProcessDto> getById(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientTransportProcessService.getById(id));
    }

    @PostMapping("/{id}/startingTransport/")
    public void startingTransport(@PathVariable("id") Long id) throws MessagingException, UnsupportedEncodingException {
      clientTransportProcessService.startingTransport(id);
    }
    @PostMapping("/{id}/endingTransport/")
    public void endingTransport(@PathVariable("id") Long id) throws MessagingException, UnsupportedEncodingException {
        clientTransportProcessService.endingTransport(id);
    }

    @PermitAllCustom
    @PostMapping("/startConfirmationTransport")
    public void startTransport(@RequestParam("token")String token) throws MessagingException, UnsupportedEncodingException {
        clientTransportProcessService.startTransportConfirmToken(token);
    }

    @PermitAllCustom
    @PostMapping("/endConfirmationTransport")
    public void endTransport(@RequestParam("token")String token) throws MessagingException, UnsupportedEncodingException {
        clientTransportProcessService.endTransportConfirmToken(token);
    }
    @PutMapping("/containerReceived")
    public void containerReceived(@RequestParam String transportCode){
      clientTransportProcessService.containerReceived(transportCode);
    }
    @PutMapping("/cargoDelivered")
    public void containerDelivered(@RequestParam String transportCode){
        clientTransportProcessService.cargoDelivered(transportCode);
    }

    @GetMapping("/clientAdvertisement/{id}")
    public ResponseEntity<ClientTransportProcessDto> getByClientAdvertisementId(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientTransportProcessService.findByClientAdvertisementId(id));

    }
    @PermitAllCustom
    @GetMapping("/transportCode/{transportCode}")
    public ResponseEntity<ClientTransportProcessDto> getByTransportCode(@PathVariable(name = "transportCode") String transportCode) {

        return ResponseEntity.ok(clientTransportProcessService.getByTransportCode(transportCode));

    }

    @GetMapping("{id}/document")
    public List<DocumentResponse> getDocumentList(@PathVariable Long id){
       return clientTransportProcessService.getDocumentList(id);
    }
    @GetMapping("/clientAdvertisementBid/{id}")
    public ResponseEntity<ClientTransportProcessDto> getByClientAdvertisementBidId(@PathVariable(name = "id") Long id) {

        return ResponseEntity.ok(clientTransportProcessService.findByAcceptedClientAdvertisementBidId(id));

    }

    @GetMapping("/{id}/sendSms")
    public void sendSms(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        clientTransportProcessService.sendSms(id, getSiteURL(request));
    }

    @GetMapping()
    public ResponseEntity<List<ClientTransportProcessDto>> getAll() {

        return ResponseEntity.ok(clientTransportProcessService.getAll());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Long id) {

        clientTransportProcessService.deleteById(id);
    }

    @PostMapping("/{clientTransportProcessId}/vehicle")
    public ResponseEntity<ClientTransportProcessDto> assignVehicle(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId, @RequestParam Long vehicleId) {

        return ResponseEntity.ok(clientTransportProcessService.assignVehicle(clientTransportProcessId, vehicleId));
    }

    @GetMapping("/myTransportProcesses")
    public ResponseEntity<List<ClientTransportProcessDto>> getMyTransportProcesses() {

        return ResponseEntity.ok(clientTransportProcessService.getMyTransportProcesses());
    }


    //Client kargoyu yollamak için teslim ederken qr kod oluşturacak. Bunun için client tarafında ödemenin yapılmış olması lazım.
    @GetMapping("/{clientTransportProcessId}/receivingCargoFromClient")
    public ResponseEntity<String> receivingCargoFromClient(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId) {

        return ResponseEntity.ok(clientTransportProcessService.receivingCargoFromClient(clientTransportProcessId));
    }

    //Driver, Client'ın oluşturduğu qrCode'u verify ediyor.  Bunun için müşteri tarafında ödemenin yapılmış olması lazım.
    @GetMapping("/verifyReceiveQrCode")
    public ResponseEntity<Boolean> verifyReceiveQrCode(@RequestParam String receiveQrCode) {

        return ResponseEntity.ok(clientTransportProcessService.verifyReceiveQrCode(receiveQrCode));
    }

    @GetMapping("/acceptReceive")
    public ResponseEntity<Boolean> acceptReceive(@RequestParam String transportCode) {

        return ResponseEntity.ok(clientTransportProcessService.acceptReceive(transportCode));
    }

    @GetMapping("/startTransport/transportCode/{transportCode}")
    public ResponseEntity<Boolean> startTransportWithTransportCode(@PathVariable("transportCode") String transportCode) {

        return ResponseEntity.ok(clientTransportProcessService.startTransportWithTransportCode(transportCode));
    }
    @GetMapping("/endTransport/transportCode/{transportCode}")
    public ResponseEntity<Boolean> endTransportWithTransportCode(@PathVariable("transportCode") String transportCode) {

        return ResponseEntity.ok(clientTransportProcessService.endTransportWithTransportCode(transportCode));
    }

    /*
    //Driver yolculuğa başladığı esnada buraya istek atıyor ki transport statüsü yolda olarak güncellensin.
    @GetMapping("/{clientTransportProcessId}/startTransport")
    public ResponseEntity<Boolean> startTransport(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId) {

        return ResponseEntity.ok(transportProcessService.startTransport(clientTransportProcessId));
    }
     */


    //Client Kargoyu Teslim Alacagi Sırada Bu İsteği Atar ve Qr Code Oluşturur.
    @GetMapping("/{clientTransportProcessId}/cargoDeliverToClient")
    public ResponseEntity<String> cargoDeliverToClient(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId) {

        return ResponseEntity.ok(clientTransportProcessService.cargoDeliverToClient(clientTransportProcessId));
    }


    //Driver Çeşitli Sebeplerden Dolayı Kargoyu Teslim Edemediği Zaman Buraya İstek Atar ve Açıklamasını Da Yollar.
    @PostMapping("/{clientTransportProcessId}/cargoNotDelivered")
    public ResponseEntity<Boolean> cargoCouldNotDelivered(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId, @RequestBody String explanation) {

        return ResponseEntity.ok(clientTransportProcessService.cargoCouldNotDelivered(clientTransportProcessId, explanation));
    }

    //Driver, kargoyu Client'a teslim ettiği zaman Qr Code'u Verify Eder.
    @GetMapping("/verifyDeliverQrCode")
    public ResponseEntity<Boolean> verifyDeliverQrCode(@RequestParam String deliverQrCode) {

        return ResponseEntity.ok(clientTransportProcessService.verifyDeliverQrCode(deliverQrCode));
    }

    // Müşteri Teslim Aldığı Kargonun Sağlamlığına Bakar ve Buraya İstek Atar. Eğer Kırık Dökük Bir Şey Varsa İşlemi Onaylamaz ve Açıklamasını Yazarak İstek Atar.
    // Eğer Başarılı Bir Şekilde Sonuçlanırsa Şirketin Banka Hesabına Para Aktarma Fonksiyonuna Yönlendirilir.
    @PostMapping("/{clientTransportProcessId}/cargoCompletedCheck")
    public ResponseEntity<String> clientCargoCompletedCheck(@PathVariable(value = "clientTransportProcessId") Long clientTransportProcessId, @RequestBody ClientCargoCompletedCheckDto clientCargoCompletedCheckDto) {

        return ResponseEntity.ok(clientTransportProcessService.clientCargoCompletedCheck(clientTransportProcessId, clientCargoCompletedCheckDto.getIsSuccess(), clientCargoCompletedCheckDto.getExplanation()));
    }

    @PostMapping("/{id}/clientDocument")
    public void uploadClientTransportProcessDocumentFile(FileUploadUtil.FileResult result, @PathVariable Long id, @RequestParam DocumentType documentType){
        SaveDocumentFileRequest saveDocumentFileRequest = new SaveDocumentFileRequest();
        saveDocumentFileRequest.setDocumentType(documentType);
        saveDocumentFileRequest.setFileName(result.getFilename());
        clientTransportProcessService.uploadClientTransportProcessDocumentFile(result,id,saveDocumentFileRequest);
    }
    @PostMapping("/{id}/driverDocument")
    public void uploadDriverTransportProcessDocumentFile(FileUploadUtil.FileResult result, @PathVariable Long id, @RequestParam DocumentType documentType){
        SaveDocumentFileRequest saveDocumentFileRequest = new SaveDocumentFileRequest();
        saveDocumentFileRequest.setDocumentType(documentType);
        saveDocumentFileRequest.setFileName(result.getFilename());
        clientTransportProcessService.uploadDriverTransportProcessDocumentFile(result,id,saveDocumentFileRequest);
    }



    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }


}






