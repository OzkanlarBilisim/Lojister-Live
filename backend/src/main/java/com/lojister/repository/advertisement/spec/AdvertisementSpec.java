package com.lojister.repository.advertisement.spec;

import com.lojister.model.entity.client.ClientAdvertisement;
import com.lojister.repository.common.spec.SearchFilter;
import com.lojister.repository.common.spec.RequiredType;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public class AdvertisementSpec {

    private static Specification<ClientAdvertisement> createSpecification(SearchFilter input) {

        switch (input.getOperator()){

            case EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(input.getField()),
                                RequiredType.castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case NOT_EQUALS:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.notEqual(root.get(input.getField()),
                                RequiredType.castToRequiredType(root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case GREATER_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.gt(root.get(input.getField()),
                                (Number) RequiredType.castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case LESS_THAN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.lt(root.get(input.getField()),
                                (Number) RequiredType.castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValue()));

            case LIKE:
                return (root, query, criteriaBuilder) ->

                        criteriaBuilder.like(root.get(input.getField()),
                                "%"+input.getValue()+"%");

            case IN:
                return (root, query, criteriaBuilder) ->
                        criteriaBuilder.in(root.get(input.getField()))
                                .value(RequiredType.castToRequiredType(
                                        root.get(input.getField()).getJavaType(),
                                        input.getValues()));

            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    public static Specification<ClientAdvertisement> getSpecificationFromFilters(List<SearchFilter> searchFilter){
        Specification<ClientAdvertisement> specification =
                where(createSpecification(searchFilter.remove(0)));
        for (SearchFilter input : searchFilter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }
}
