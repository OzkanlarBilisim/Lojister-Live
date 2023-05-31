package com.lojister.repository.api;

import com.lojister.model.entity.TcmbData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TcmbApiRepository extends JpaRepository<TcmbData, Long> {
}
