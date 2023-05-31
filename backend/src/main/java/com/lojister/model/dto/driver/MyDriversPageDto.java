package com.lojister.model.dto.driver;

import com.lojister.model.enums.DriverStatus;
import com.lojister.core.helper.ContentTypeHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MyDriversPageDto {

    private Long id;

    private LocalDateTime createdDateTime;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private DriverStatus status;

    private String statusDescription;

    private String citizenId;

    private String profilePhoto;

    private String fileName;

    private String type;

    private String licencePlate;


    public MyDriversPageDto(Long id,LocalDateTime createdDateTime, String firstName, String lastName, String phone,
                            String email, DriverStatus status, String statusDescription,
                            String citizenId, Blob profilePhoto, String fileName, String licencePlate) {
        this.id = id;
        this.createdDateTime=createdDateTime;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.status = status;
        this.statusDescription = statusDescription;
        this.citizenId = citizenId;
        this.licencePlate = licencePlate;

        try {
            if (profilePhoto != null) {

                String ext = FilenameUtils.getExtension(fileName);
                String contentType = ContentTypeHelper.getDataByContentType(ext);

                InputStream is = profilePhoto.getBinaryStream();
                byte[] bytes = IOUtils.toByteArray(is);
                String encoded = Base64.encodeBase64String(bytes);

                this.profilePhoto = encoded;
                this.fileName = fileName;
                this.type = contentType;

            } else {
                this.profilePhoto = null;
                this.fileName = null;
                this.type = null;
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
