package com.lojister.mapper;

import com.lojister.model.dto.dynamic.FrequentlyAskedQuestionsDto;
import com.lojister.model.entity.adminpanel.FrequentlyAskedQuestions;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FrequentlyAskedQuestionsMapper extends BaseMapper<FrequentlyAskedQuestionsDto, FrequentlyAskedQuestions> {
}
