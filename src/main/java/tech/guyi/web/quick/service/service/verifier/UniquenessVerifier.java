package tech.guyi.web.quick.service.service.verifier;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface UniquenessVerifier<E> {

    Predicate apply(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder,E entity);

}
