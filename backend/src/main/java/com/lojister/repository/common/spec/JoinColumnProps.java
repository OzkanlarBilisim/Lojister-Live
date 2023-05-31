package com.lojister.repository.common.spec;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinColumnProps {
    private String joinColumnName;
    private SearchFilter searchFilter;
}
