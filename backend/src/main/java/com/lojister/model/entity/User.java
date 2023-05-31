package com.lojister.model.entity;

import com.lojister.model.enums.Language;
import com.lojister.model.enums.Role;
import com.lojister.model.entity.base.AbstractTimestampEntity;
import com.lojister.model.enums.UserRegionType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class User extends AbstractTimestampEntity {

    private String firstName;

    private String lastName;

    @NotEmpty
    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String email;

    //driver ve clinet d olacak
    private Boolean phoneConfirmed;

    //driver ve clinet d olacak
    private Boolean mailConfirmed;

    //Spring Boot Security için tutuyorum. Silme
    private Boolean enabled = true;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String firebaseToken;



    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToMany(mappedBy = "user")
    List<SavedAddress> savedAddresses;

    @Enumerated(value = EnumType.STRING)
    private UserRegionType userRegionType;
}
