package com.lojister.repository.driver;

import com.lojister.model.abroudModel.AdAbroud;
import com.lojister.model.dto.Token;
import com.lojister.model.dto.driver.MyDriversPageDto;
import com.lojister.model.enums.DriverStatus;
import com.lojister.model.enums.DriverTitle;
import com.lojister.model.entity.driver.Driver;
import com.lojister.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Token> findAllByDriverTitle(DriverTitle driverTitle);

    Optional<Driver> findByCompany_IdAndDriverTitle(Long id, DriverTitle driverTitle);

    List<Driver> findDistinctByStatus(DriverStatus driverStatus);
    List<Driver> findDistinctByStatusIn(List<DriverStatus> driverStatuses);
    List<Driver> findByDriverTitleIn(List<DriverTitle> driverTitleList);
    // COALESCE(pp.data,'')
    //@Query(value = "select new com.lojister.model.dto.driver.MyDriversPageDto(dr.id, dr.firstName, dr.lastName, dr.phone, dr.email, dr.status, dr.statusDescription, dr.citizenId, dr.rating, pp.data, v.licencePlate) " +
    //        "from " + "Driver dr, ProfilePhotoFile pp, Vehicle v " + "where " +"dr.id=:id and pp.user.id=:id ")
    //List<MyDriversPageDto> getMyDrivers(@Param(value = "id") Long id);

   /* @Query(value = "SELECT new com.lojister.model.dto.driver.MyDriversPageDto(dr, pp , v) FROM Driver dr LEFT JOIN ProfilePhotoFile pp ON pp.user.id=dr.id LEFT JOIN Vehicle v ON v.driver.id = dr.id WHERE dr.company.id = :companyId")
    List<MyDriversPageDto> getMyDriversExample(@Param(value = "companyId") Long companyId);
    */

    @Query(value = "SELECT new com.lojister.model.dto.driver.MyDriversPageDto(dr.id,dr.createdDateTime, dr.firstName, dr.lastName, dr.phone, dr.email, dr.status, dr.statusDescription, dr.citizenId , pp.data,pp.fileName, v.licencePlate) " +
            "FROM Driver dr LEFT JOIN ProfilePhotoFile pp ON pp.user.id=dr.id LEFT JOIN Vehicle v ON v.driver.id = dr.id WHERE dr.company.id = :companyId")
    Page<MyDriversPageDto> getMyDriversByCompanyId(@Param(value = "companyId") Long companyId, Pageable pageable);

    @Query(value = "SELECT dr " +
            "FROM Driver dr LEFT JOIN ProfilePhotoFile pp ON pp.user.id=dr.id LEFT JOIN Vehicle v ON v.driver.id = dr.id WHERE dr.company.id = :companyId")
    List<Driver> getMyDriversByCompanyId(@Param(value = "companyId") Long companyId);

    @Query("SELECT u FROM Driver u WHERE u.company.id = :companyID")
    Optional<Driver> findByCompanyID(@Param(value = "companyID") Long companyID);

}
