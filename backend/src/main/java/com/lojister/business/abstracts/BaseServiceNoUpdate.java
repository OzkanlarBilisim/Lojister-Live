package com.lojister.business.abstracts;

import java.util.List;

public interface BaseServiceNoUpdate<Dto> {

    Dto save(Dto dto);

    Dto getById(Long id);

    void deleteById(Long id);

    List<Dto> getAll();

}
