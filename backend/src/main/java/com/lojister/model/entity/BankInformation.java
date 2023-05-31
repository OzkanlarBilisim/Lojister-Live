package com.lojister.model.entity;

import com.lojister.model.entity.base.AbstractTimestampEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankInformation extends AbstractTimestampEntity {

    @ColumnDefault("''")
    private String accountName;

    @ColumnDefault("''")
    private String bankName;

    @ColumnDefault("''")
    private String branch;

    @ColumnDefault("''")
    private String accountNumber;

    @ColumnDefault("''")
    private String iban;

}
