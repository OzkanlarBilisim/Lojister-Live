package com.lojister.repository.payment;

import com.lojister.model.entity.payment.RegisteredCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisteredCardsRepository extends JpaRepository<RegisteredCards, Long> {
    @Query("SELECT u FROM RegisteredCards u WHERE u.userId.id = :userID ")
    List<RegisteredCards> findByUserId(Long userID);

}
