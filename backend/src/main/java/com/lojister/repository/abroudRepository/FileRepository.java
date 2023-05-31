package com.lojister.repository.abroudRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.lojister.model.abroudModel.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("SELECT u FROM File u WHERE u.name = :name AND u.view = 1")
    Optional<File> getFileName(@Param(value = "name") String name);

    @Query("SELECT u FROM File u WHERE u.adAbroud.id = :advert_ID AND u.view = 1 AND u.role = :role")
    List<File> listFile(@Param(value = "role") String role, @Param(value = "advert_ID") int advert_ID);
}
