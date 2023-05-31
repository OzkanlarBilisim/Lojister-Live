package com.lojister.repository.account;

import com.lojister.model.entity.ProfilePhotoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePhotoFileRepository extends JpaRepository<ProfilePhotoFile, Long> {

    Optional<ProfilePhotoFile> findByUser_Id(Long id);
}
