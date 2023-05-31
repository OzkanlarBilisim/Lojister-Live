package com.lojister.controller.account;

import com.lojister.model.entity.Address;
import com.lojister.model.enums.Language;
import com.lojister.model.enums.UserRegionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {

    // individual or corporate
    @ApiModelProperty(value="individual or corporate", required = true,allowableValues = "individual,corporate",dataType = "string")
    @NotNull
    private String personType;

    @NotNull(message = "{lojister.constraint.register.firstName.NotNull.message}")
    @Size(min = 2,message = "lojister.constraint.register.firstName.Size.message")
    private String firstName;

    @NotNull(message = "{lojister.constraint.register.lastName.NotNull.message}")
    @Size(min = 2,message = "{lojister.constraint.register.lastName.Size.message}")
    private String lastName;

    @NotNull(message = "{lojister.constraint.register.password.NotNull.message}")
    @Size(min = 8,max = 32,message = "{lojister.constraint.register.password.Size.message}")
    private String password;

    @NotNull(message = "{lojister.constraint.register.phone.NotNull.message}")
    @Size(min =13,max = 20,message = "{lojister.constraint.register.phone.Size.message}")
    private String phone;

    @NotNull(message = "{lojister.constraint.register.email.NotNull.message}")
    @Email(message = "{lojister.constraint.register.email.Email.message}")
    private String email;

    private String commercialTitle;

    private String taxNumber;
    private Language language;
    private String taxAdministration;
    private UserRegionType userRegionType;
    @Embedded
    private Address address;

}
