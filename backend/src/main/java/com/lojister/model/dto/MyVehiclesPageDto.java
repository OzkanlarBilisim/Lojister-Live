package com.lojister.model.dto;

import com.lojister.model.dto.driver.DriverForVehicleDto;
import com.lojister.model.dto.dynamic.VehicleTypeDto;
import com.lojister.model.entity.adminpanel.VehicleType;
import com.lojister.model.enums.VehicleStatus;
import com.lojister.core.helper.ContentTypeHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MyVehiclesPageDto {

    private Long id;

    private LocalDateTime createdDateTime;

    private String vehicleName;

    private String licencePlate;

    private String trailerPlate;

    private Double maxCapacity;

    private VehicleStatus vehicleStatus;

    private String statusDescription;

    private String vehicleModel;

    private Long vehicleTypeId;

    private String vehicleTypeName;


    private String brand;



    private DriverForVehicleDto driver;

    public MyVehiclesPageDto(Long id,LocalDateTime createdDateTime, String vehicleName, String licencePlate, String trailerPlate,
                             Double maxCapacity, VehicleStatus vehicleStatus,
                             String statusDescription, String driverFirstName, String driverLastName,
                             Blob profilePhoto, String profilePhotoFileName,String vehicleModel,Long vehicleTypeId, String vehicleTypeName,String brand) {
        this.id = id;
        this.createdDateTime=createdDateTime;
        this.vehicleName = vehicleName;
        this.licencePlate = licencePlate;
        this.trailerPlate = trailerPlate;
        this.maxCapacity = maxCapacity;
        this.vehicleStatus = vehicleStatus;
        this.statusDescription = statusDescription;
        this.vehicleModel = vehicleModel;
        this.vehicleTypeId = vehicleTypeId;
        this.vehicleTypeName = vehicleTypeName;
        this.brand = brand;

        DriverForVehicleDto driverForVehicleDto = new DriverForVehicleDto();

        if (StringUtils.isNotBlank(driverFirstName) && StringUtils.isNotBlank(driverLastName)) {
            driverForVehicleDto.setFirstName(driverFirstName);
            driverForVehicleDto.setLastName(driverLastName);

            try {
                if (profilePhoto != null) {

                    String ext = FilenameUtils.getExtension(profilePhotoFileName);
                    String contentType = ContentTypeHelper.getDataByContentType(ext);

                    InputStream is = profilePhoto.getBinaryStream();
                    byte[] bytes = IOUtils.toByteArray(is);
                    String encoded = Base64.encodeBase64String(bytes);

                    driverForVehicleDto.setProfilePhoto(encoded);
                    driverForVehicleDto.setProfilePhotoFileName(profilePhotoFileName);
                    driverForVehicleDto.setProfilePhotoType(contentType);

                } else {
                    driverForVehicleDto.setProfilePhoto(null);
                    driverForVehicleDto.setProfilePhotoFileName(null);
                    driverForVehicleDto.setProfilePhotoType(null);
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            driverForVehicleDto = null;
        }
        this.driver = driverForVehicleDto;

    }
}
