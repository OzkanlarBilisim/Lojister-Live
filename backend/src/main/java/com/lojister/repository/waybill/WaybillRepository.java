package com.lojister.repository.waybill;

import com.lojister.model.dto.WaybillWithoutDataWithEntityDto;
import com.lojister.model.enums.WaybillStatus;
import com.lojister.model.entity.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaybillRepository extends JpaRepository<Waybill, Long> {

    Optional<Waybill> findByClientTransportProcess_Id(Long transportProcessId);

    @Query(value = "select new com.lojister.model.dto.WaybillWithoutDataWithEntityDto(wb.id,wb.clientTransportProcess,wb.waybillStatus) " + "from " + "Waybill  wb " + "where " + "wb.waybillStatus=:status")
    List<WaybillWithoutDataWithEntityDto> findNotDataByWaybillStatus(@Param(value = "status") WaybillStatus waybillStatus);

}
