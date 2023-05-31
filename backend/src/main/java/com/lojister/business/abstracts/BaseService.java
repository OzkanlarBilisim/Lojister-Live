package com.lojister.business.abstracts;

import java.util.List;
import java.util.Set;

public interface BaseService<Dto> {

    Dto save(Dto dto);

    Dto update(Long id, Dto dto);

    Dto getById(Long id);

    void deleteById(Long id);

    List<Dto> getAll();

}
