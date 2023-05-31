package com.lojister.repository.site;

import com.lojister.model.entity.adminpanel.FrequentlyAskedQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequentlyAskedQuestionsRepository extends JpaRepository<FrequentlyAskedQuestions,Long> {


}
