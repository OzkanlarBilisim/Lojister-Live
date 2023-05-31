package com.lojister.repository.common.spec;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.lojister.repository.common.spec.JoinColumnProps;
import com.lojister.repository.common.spec.RequiredType;
import com.lojister.repository.common.spec.SearchFilter;
import com.lojister.repository.common.spec.SearchQuery;
import org.springframework.data.jpa.domain.Specification;

import static antlr.build.ANTLR.root;


public class SpecificationUtil {
    public static <T> Specification<T> bySearchQuery(SearchQuery searchQuery, Class<T> clazz) {

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criterailBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Add Predicates for tables to be joined
            List<JoinColumnProps> joinColumnProps = searchQuery.getJoinColumnProps();

            if (joinColumnProps != null && !joinColumnProps.isEmpty()) {
                for (JoinColumnProps joinColumnProp : joinColumnProps) {
                    addJoinColumnProps(predicates, joinColumnProp, criterailBuilder, root);
                }
            }

            List<SearchFilter> searchFilters = searchQuery.getSearchFitler();

            if (searchFilters != null && !searchFilters.isEmpty()) {

                for (final SearchFilter searchFilter : searchFilters) {
                    addPredicates(predicates, searchFilter, criterailBuilder, root);
                }
            }

            if (predicates.isEmpty()) {
                return criterailBuilder.conjunction();
            }

            return criterailBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

    private static <T> void addJoinColumnProps(List<Predicate> predicates, JoinColumnProps joinColumnProp,
                                               CriteriaBuilder criterailBuilder, Root<T> root) {

        SearchFilter searchFilter = joinColumnProp.getSearchFilter();
        Join<Object, Object> joinParent = root.join(joinColumnProp.getJoinColumnName());

        String property = searchFilter.getField();
        Path expression = joinParent.get(property);

        addPredicate(predicates, searchFilter, criterailBuilder, expression);

    }

    private static <T> void addPredicates(List<Predicate> predicates, SearchFilter searchFilter,
                                          CriteriaBuilder criterailBuilder, Root<T> root) {
        String property = searchFilter.getField();
        Path expression = root.get(property);

        addPredicate(predicates, searchFilter, criterailBuilder, expression);

    }

    private static void addPredicate(List<Predicate> predicates, SearchFilter searchFilter,
                                     CriteriaBuilder criterailBuilder, Path expression) {
        switch (searchFilter.getOperator()) {
            case EQUALS:
                predicates.add(criterailBuilder.equal(expression, searchFilter.getValue()));
                break;
            case LIKE:
                predicates.add(criterailBuilder.like(expression, "%" + searchFilter.getValue() + "%"));
                break;
            case IN:
                predicates.add(criterailBuilder.in(expression).value(RequiredType.castToRequiredType(
                        expression.getJavaType(),
                        searchFilter.getValues())));
                break;
            case  GREATER_THAN:
                predicates.add(criterailBuilder.greaterThan(expression, (Comparable) searchFilter.getValue()));
                break;
            case LESS_THAN:
                predicates.add(criterailBuilder.lessThan(expression, (Comparable) searchFilter.getValue()));
                break;
        /*    case ">=":
                predicates.add(criterailBuilder.greaterThanOrEqualTo(expression, (Comparable) searchFilter.getValue()));
                break;
            case "<=":
                predicates.add(criterailBuilder.lessThanOrEqualTo(expression, (Comparable) searchFilter.getValue()));
                break;*/
            case NOT_EQUALS:
                predicates.add(criterailBuilder.notEqual(expression, searchFilter.getValue()));
                break;
        /*    case "IsNull":
                predicates.add(criterailBuilder.isNull(expression));
                break;
            case "NotNull":
                predicates.add(criterailBuilder.isNotNull(expression));
                break;*/
            default:
                System.out.println("Predicate is not matched");
                throw new IllegalArgumentException(searchFilter.getOperator() + " is not a valid predicate");
        }

    }

}
