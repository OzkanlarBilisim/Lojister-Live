package com.lojister.repository.advertisement;

import com.lojister.model.entity.SavedClientAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedClientAdvertisementRepository extends JpaRepository<SavedClientAdvertisement,Long> {

    List<SavedClientAdvertisement> findByClient_IdOrderByIdDesc(Long id);

}
