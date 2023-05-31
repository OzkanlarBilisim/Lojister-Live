package com.lojister.business.concretes;

import com.lojister.core.i18n.Translator;
import com.lojister.core.util.annotation.OnlyAdmin;
import com.lojister.model.dto.dynamic.AboutUsDto;
import com.lojister.core.exception.EntityNotFoundException;
import com.lojister.mapper.AboutUsMapper;
import com.lojister.model.entity.adminpanel.AboutUs;
import com.lojister.repository.site.AboutUsRepository;
import com.lojister.business.abstracts.dynamic.AboutUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AboutUsServiceImpl implements AboutUsService {

    private final AboutUsRepository aboutUsRepository;
    private final AboutUsMapper aboutUsMapper;

    @Override
    public AboutUsDto save(AboutUsDto aboutUsDto) {

        AboutUs aboutUs = new AboutUs();
        aboutUs.setTr_explanation(aboutUsDto.getTr_explanation());
        aboutUs.setEng_explanation(aboutUsDto.getEng_explanation());
        aboutUs = aboutUsRepository.save(aboutUs);

        return aboutUsMapper.entityToDto(aboutUs);
    }

    @Override
    @OnlyAdmin
    public AboutUsDto updateEng(AboutUsDto aboutUsDto) {
        Optional<AboutUs> aboutUs = aboutUsRepository.findFirstByOrderByIdAsc();

        if (aboutUs.isPresent()) {

            aboutUs.get().setEng_explanation(aboutUsDto.getEng_explanation());
            return aboutUsMapper.entityToDto(aboutUsRepository.save(aboutUs.get()));
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.aboutUs.EntityNotFoundException"));
        }
    }

    @Override
    @OnlyAdmin
    public AboutUsDto updateTr(AboutUsDto aboutUsDto) {
        Optional<AboutUs> aboutUs = aboutUsRepository.findFirstByOrderByIdAsc();

        if (aboutUs.isPresent()) {

            aboutUs.get().setTr_explanation(aboutUsDto.getTr_explanation());

            return aboutUsMapper.entityToDto(aboutUsRepository.save(aboutUs.get()));
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.aboutUs.EntityNotFoundException"));
        }
    }



    @Override
    public AboutUsDto getById(Long id) {

        Optional<AboutUs> aboutUs = aboutUsRepository.findById(id);

        if (aboutUs.isPresent()) {

            return aboutUsMapper.entityToDto(aboutUs.get());
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.aboutUs.EntityNotFoundException"));
        }
    }

    private void engNullCheck(AboutUs aboutUs){
        if (aboutUs.getEng_explanation()==null){
            aboutUs.setEng_explanation(aboutUs.getTr_explanation());
        }
    }

    @Override
    public AboutUsDto getAboutUs() {
        Optional<AboutUs> aboutUs = aboutUsRepository.findFirstByOrderByIdAsc();

        if (aboutUs.isPresent()) {

            engNullCheck(aboutUs.get());

            return aboutUsMapper.entityToDto(aboutUs.get());
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.aboutUs.EntityNotFoundException"));
        }
    }

    @Override
    public void deleteById(Long id) {

        Optional<AboutUs> aboutUs = aboutUsRepository.findById(id);

        if (aboutUs.isPresent()) {

            aboutUsRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(Translator.toLocale("lojister.aboutUs.EntityNotFoundException"));
        }

    }

    @Override
    public List<AboutUsDto> getAll() {
        return aboutUsMapper.entityListToDtoList(aboutUsRepository.findAll());
    }
}
