package com.lojister.repository.address;

import com.lojister.model.entity.SavedAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedAddressRepository extends JpaRepository<SavedAddress,Long> {

    List<SavedAddress> findByUser_Id(Long id);
    Optional<SavedAddress> findByUser_IdAndIsDefaultAddress(Long id, Boolean isDefaultAddress);

}
