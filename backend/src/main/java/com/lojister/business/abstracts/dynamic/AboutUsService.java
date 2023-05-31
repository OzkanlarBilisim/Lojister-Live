package com.lojister.business.abstracts.dynamic;

import com.lojister.model.dto.dynamic.AboutUsDto;

import java.util.List;

public interface AboutUsService {

    AboutUsDto save(AboutUsDto dto);

    AboutUsDto updateEng(AboutUsDto dto);

    AboutUsDto updateTr(AboutUsDto dto);

    AboutUsDto getById(Long id);

    AboutUsDto getAboutUs();

    void deleteById(Long id);

    List<AboutUsDto> getAll();

}
