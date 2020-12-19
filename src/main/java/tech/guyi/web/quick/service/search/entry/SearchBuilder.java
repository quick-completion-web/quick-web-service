package tech.guyi.web.quick.service.search.entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author guyi
 * 搜索条件创建器
 */
@FunctionalInterface
public interface SearchBuilder {

    <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item);

}
