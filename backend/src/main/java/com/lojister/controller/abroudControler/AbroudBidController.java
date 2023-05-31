package com.lojister.controller.abroudControler;


import com.lojister.business.abstracts.MailNotificationService;
import com.lojister.business.concretes.UserServiceImpl;
import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.abroudModel.abroudBid;
import com.lojister.model.dto.abroudDto.AbroudBidDto;
import com.lojister.model.dto.abroudDto.AbroudBidRatingDto;
import com.lojister.model.dto.abroudDto.DriverBidList;
import com.lojister.model.dto.abroudDto.BidAndAdvertRequestDto;
import com.lojister.model.enums.Role;
import com.lojister.repository.abroudRepository.AbroudBidRepository;
import com.lojister.repository.abroudRepository.AbroudRepository;
import com.lojister.service.abroudService.AbroudBidService;
import com.lojister.service.abroudService.AbroudBidServiceImpl;
import com.lojister.service.abroudService.AbroudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping("/abroudBid")
@CrossOrigin
public class AbroudBidController {

    @Autowired
    private AbroudBidService studentService;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    SecurityContextUtil securityContextUtil;
    @Autowired
    private AbroudBidRepository abroudBidRepository;
    @Autowired
    private AbroudRepository abroudRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private AbroudServiceImpl abroudServiceImpl;

    @PostMapping("/add")
    public String add(@RequestBody abroudBid student){
        studentService.saveStudent(student);
        return "Yeni ğrenci eklendi";
    }
    @GetMapping("/adwertisamentId/{adwertisamentId}")
    public List<AbroudBidRatingDto> getAllStudents(@PathVariable int adwertisamentId){
        return studentService.getAllAdwertismantId(adwertisamentId);
    }

    @GetMapping("/getFindMyBid/{bidID}/{companyID}")
    public AbroudBidDto getFindMyBid(@PathVariable int bidID, @PathVariable int companyID){
        return studentService.getFindMyBid(bidID, companyID);
    }

/*
    @GetMapping("/cerez/set")
    public String setCookie(HttpServletResponse response, String user_ıd) {
        Cookie cookie = new Cookie("myCookie", user_ıd);
        cookie.setMaxAge(-1); // 1 hour
        response.addCookie(cookie);
        return "Cookie is set!";
    }

    @GetMapping("/cerez/get")
    public String getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("myCookie")) {
                    String value = cookie.getValue();
                    return "Cookie value: " + value;
                }
            }
        }
        return "Cookie not found!";
    }


    @GetMapping("/cerez/del")
    public String deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("myCookie")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    return "Cookie deleted!";
                }
            }
        }
        return "Cookie not found!";
    }
*/














    @GetMapping("/driver/bid/list/{driverId}")
    public List<DriverBidList> driverBidList(@PathVariable int driverId){
        return studentService.driverBidList(driverId);
    }

    @GetMapping("/getBidApproved/{id}")
    public List<abroudBid> getBidApproved(@PathVariable int id){
        return abroudBidRepository.getBidApproved(id);
    }

    @GetMapping("/companyId/{companyId}/adwertisamentId/{adwertisamentId}")
    public List <abroudBid> findClientAbroudt(@PathVariable int companyId, @PathVariable int adwertisamentId){
        return studentService.findClientAbroud(companyId, adwertisamentId);
    }

    @GetMapping("/bid/and/advert/request/{bidID}")
    public BidAndAdvertRequestDto bidAndAdvertRequestDto(@PathVariable int bidID){
        return studentService.bidAndAdvertRequestDto(bidID);
    }


    @PutMapping("/statusUpdate/{id}")
    public ResponseEntity<abroudBid> updateEmployee(@PathVariable int id, @RequestBody abroudBid employeeDetails) {
        abroudBid updateEmployee = abroudBidRepository.findById(id).orElseThrow((null));

        updateEmployee.setStatus(employeeDetails.getStatus());

        abroudBidRepository.save(updateEmployee);

        return ResponseEntity.ok(updateEmployee);
    }

    @PutMapping("/statusUpdateDenied/{id}")
    public ResponseEntity<abroudBid> updateEmployeeDenied(@PathVariable int id) {
        ResponseEntity<abroudBid> RReturn = null;

        // Doğru rolde bir kullanıcı mı diye kontrol ediyor
        if (securityContextUtil.getCurrentUserRole().equals(Role.ROLE_CLIENT)){
            abroudBid selectBid = abroudBidRepository.IdFind(id).get(0);

            // Doğru kullanıcı mı diye kontrol ediyor
            if(securityContextUtil.getCurrentUserId().equals(Long.valueOf(selectBid.getAdAbroud().getClient_id()))){
                selectBid.setStatus("DENIED");

                abroudBidRepository.save(selectBid);

                RReturn = ResponseEntity.ok(selectBid);
            }
        }
        return RReturn;
    }
    @PutMapping("/statusApproved/{id}")
    public ResponseEntity<abroudBid> updateEmployeeApproved(@PathVariable int id) {

        ResponseEntity<abroudBid> RReturn = null;

        // Doğru rolde bir kullanıcı mı diye kontrol ediyor
        if (securityContextUtil.getCurrentUserRole().equals(Role.ROLE_CLIENT)){
            abroudBid selectBid = abroudBidRepository.IdFind(id).get(0);
            // Doğru kullanıcı mı diye kontrol ediyor
            if(securityContextUtil.getCurrentUserId().equals(Long.valueOf(selectBid.getAdAbroud().getClient_id()))){
                List<abroudBid> upduteBid = abroudBidRepository.getAllAdwertismantId(selectBid.getAdAbroud().getId());

                for (abroudBid bid : upduteBid){
                    if(bid.getId() == id){
                        mailNotificationService.approvedMailSend(bid.getId());
                        bid.setStatus("PAYMENT_SUCCESSFUL");
                        /*bid.setStatus("APPROVED");*/
                    }else {
                        bid.setStatus("DENIED");
                    }

                    abroudBidRepository.save(bid);
                }

                abroudServiceImpl.advertStatusStep(selectBid.getAdAbroud().getId());

                RReturn =  ResponseEntity.ok(selectBid);
            }
        }
        return RReturn;
    }


}
