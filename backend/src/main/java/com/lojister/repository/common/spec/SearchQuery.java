package com.lojister.repository.common.spec;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchQuery {
    private List<SearchFilter> searchFitler;
    private List<JoinColumnProps> joinColumnProps;
}
