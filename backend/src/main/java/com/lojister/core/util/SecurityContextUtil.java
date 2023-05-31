package com.lojister.core.util;

import com.lojister.model.enums.Role;
import com.lojister.core.exception.UnauthorizedTransactionException;
import com.lojister.model.entity.CustomUserDetails;
import com.lojister.model.entity.User;
import com.lojister.model.entity.client.Client;
import com.lojister.model.entity.driver.Driver;
import com.lojister.business.abstracts.ClientService;
import com.lojister.business.abstracts.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityContextUtil {


    private final DriverService driverService;
    private final ClientService clientService;

    public Boolean IsAnonymousUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    public Boolean IsAuthenticationUser(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication instanceof CustomUserDetails) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public User getCurrentUser() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }


    public Long getCurrentUserId() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }

    public Role getCurrentUserRole() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getRole();
    }

    public Driver getCurrentBossDriver() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getRole() == Role.ROLE_DRIVER) {

            return driverService.findDataById(userDetails.getUserId());

        } else {
            throw new UnauthorizedTransactionException("Yanlış ROL!!!!!");
        }

    }


    public Driver getCurrentDriver() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getRole() == Role.ROLE_DRIVER || userDetails.getRole() == Role.ROLE_DRIVER_EMPLOYEE) {

            return driverService.findDataById(userDetails.getUserId());

        } else {
            throw new UnauthorizedTransactionException("Yanlış ROL!!!!!");
        }

    }

    public Client getCurrentClient() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getRole() == Role.ROLE_CLIENT || userDetails.getRole() == Role.ROLE_CLIENT_EMPLOYEE) {

            return clientService.findDataById(userDetails.getUserId());

        } else {
            throw new UnauthorizedTransactionException("Yanlış ROL!!!!!");
        }

    }

    public void isRoleAdminOrDriver() {

        Role role = getCurrentUserRole();

        if (!(role == Role.ROLE_ADMIN || role == Role.ROLE_DRIVER)) {
            throw new UnauthorizedTransactionException("Yanlış Rolle İşlem Yaptınız.");
        }
    }


}
