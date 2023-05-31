package com.lojister.business.concretes.dynamic;

import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.FrequentlyAskedQuestionsDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.FrequentlyAskedQuestionsMapper;
import com.lojister.model.entity.adminpanel.FrequentlyAskedQuestions;
import com.lojister.repository.site.FrequentlyAskedQuestionsRepository;
import com.lojister.business.abstracts.FrequentlyAskedQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FrequentlyAskedQuestionsServiceImpl implements FrequentlyAskedQuestionsService {

    private final FrequentlyAskedQuestionsRepository frequentlyAskedQuestionsRepository;
    private final FrequentlyAskedQuestionsMapper frequentlyAskedQuestionsMapper;

    @Autowired
    public FrequentlyAskedQuestionsServiceImpl(FrequentlyAskedQuestionsRepository frequentlyAskedQuestionsRepository,
                                               FrequentlyAskedQuestionsMapper frequentlyAskedQuestionsMapper) {
        this.frequentlyAskedQuestionsRepository = frequentlyAskedQuestionsRepository;
        this.frequentlyAskedQuestionsMapper = frequentlyAskedQuestionsMapper;
    }


    @Override
    @OnlyAdmin
    public FrequentlyAskedQuestionsDto save(FrequentlyAskedQuestionsDto frequentlyAskedQuestionsDto) {

        FrequentlyAskedQuestions frequentlyAskedQuestions = new FrequentlyAskedQuestions();
        frequentlyAskedQuestions.setTr_question(frequentlyAskedQuestionsDto.getTr_question());
        frequentlyAskedQuestions.setTr_answer(frequentlyAskedQuestionsDto.getTr_answer());
        frequentlyAskedQuestions.setEng_question(frequentlyAskedQuestionsDto.getEng_question());
        frequentlyAskedQuestions.setEng_answer(frequentlyAskedQuestionsDto.getEng_answer());
        frequentlyAskedQuestions = frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions);

        return frequentlyAskedQuestionsMapper.entityToDto(frequentlyAskedQuestions);
    }

    @Override
    @OnlyAdmin
    public FrequentlyAskedQuestionsDto update(Long id, FrequentlyAskedQuestionsDto frequentlyAskedQuestionsDto) {

        FrequentlyAskedQuestions frequentlyAskedQuestions = findDataById(id);

        frequentlyAskedQuestions.setTr_question(frequentlyAskedQuestionsDto.getTr_question());
        frequentlyAskedQuestions.setTr_answer(frequentlyAskedQuestionsDto.getTr_answer());
        frequentlyAskedQuestions.setEng_question(frequentlyAskedQuestionsDto.getEng_question());
        frequentlyAskedQuestions.setEng_answer(frequentlyAskedQuestionsDto.getEng_answer());
        return frequentlyAskedQuestionsMapper.entityToDto(frequentlyAskedQuestionsRepository.save(frequentlyAskedQuestions));

    }

    @Override
    public FrequentlyAskedQuestionsDto getById(Long id) {

        FrequentlyAskedQuestions frequentlyAskedQuestions = findDataById(id);

        return frequentlyAskedQuestionsMapper.entityToDto(frequentlyAskedQuestions);

    }

    @Override
    @OnlyAdmin
    public void deleteById(Long id) {

        findDataById(id);

        frequentlyAskedQuestionsRepository.deleteById(id);
    }

    @Override
    public List<FrequentlyAskedQuestionsDto> getAll() {
        return frequentlyAskedQuestionsMapper.entityListToDtoList(frequentlyAskedQuestionsRepository.findAll());
    }

    @Override
    public FrequentlyAskedQuestions findDataById(Long id) {

        Optional<FrequentlyAskedQuestions> frequentlyAskedQuestions = frequentlyAskedQuestionsRepository.findById(id);

        if (frequentlyAskedQuestions.isPresent()) {

            return frequentlyAskedQuestions.get();

        } else {

            throw new EntityNotFoundException("İlgili Sık Sorulan Soru Bulunamadı.");

        }
    }
}
