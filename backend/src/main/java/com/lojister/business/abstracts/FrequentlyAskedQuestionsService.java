package com.lojister.business.abstracts;

import com.lojister.model.dto.dynamic.FrequentlyAskedQuestionsDto;
import com.lojister.model.entity.adminpanel.FrequentlyAskedQuestions;

public interface FrequentlyAskedQuestionsService extends BaseService<FrequentlyAskedQuestionsDto> {

    FrequentlyAskedQuestions findDataById(Long id);

}
