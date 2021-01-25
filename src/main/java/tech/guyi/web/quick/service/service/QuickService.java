package tech.guyi.web.quick.service.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import tech.guyi.web.quick.core.exception.WebRequestException;
import tech.guyi.web.quick.service.entity.QuickEntity;
import tech.guyi.web.quick.service.getter.GetRepository;
import tech.guyi.web.quick.service.search.entry.SearchItem;
import tech.guyi.web.quick.service.service.verifier.UniquenessVerifierItem;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author guyi
 * 基础Service
 * @param <E> 实体
 * @param <ID> ID类型
 */
public interface QuickService<E,ID> extends GetRepository<E,ID> {

    Class<E> entityClass();

    default List<UniquenessVerifierItem<E>> verifiers(){
        return Collections.emptyList();
    }

    default Predicate buildCondition(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem condition){
        return condition.getType().getHandler().toPredicate(root,query,builder,condition);
    }

    default Predicate[] buildConditions(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder builder, List<SearchItem> conditions) {
        return conditions
                .stream()
                .map(condition -> this.buildCondition(root,query,builder,condition))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);
    }

    default Page<E> search(List<SearchItem> conditions, Pageable pageable){
        return this.findAll(
                (root,query,builder) -> builder.and(this.buildConditions(root,query,builder,conditions)),
                pageable
        );
    }

    default void autoTime(E entity) {
        if (entity instanceof QuickEntity){
            if (((QuickEntity<?>) entity).getCreateTime() == null){
                ((QuickEntity<?>) entity).setCreateTime(System.currentTimeMillis());
            }
            ((QuickEntity<?>) entity).setUpdateTime(System.currentTimeMillis());
        }
    }

    default void autoVerifier(E entity){
        if (entity instanceof QuickEntity){
            this.verifiers().forEach(item ->
                    this.findOne((root,query,builder) -> item.getVerifier().apply(root,query,builder,entity))
                            .map(QuickEntity.class::cast)
                            .filter(e -> !e.getId().equals(((QuickEntity<?>) entity).getId()))
                            .ifPresent(e -> {
                                throw new WebRequestException(item.getMessage());
                            }));
        }
    }

    /**
     * 保存实体, 自动填充创建及更新时间
     * @see #save(E)
     */
    default E autoSave(E entity){
        this.autoVerifier(entity);
        this.autoTime(entity);
        return this.save(entity);
    }

    /**
     * @see tech.guyi.web.quick.service.repository.QuickRepository#save(Object)
     */
    default E save(E entity){
        return this.getRepository().save(entity);
    }

    /**
     * @see JpaRepository#findById(ID)
     */
    default Optional<E> findById(ID id){
        return this.getRepository().findById(id);
    }

    /**
     * @see JpaRepository#existsById(ID)
     */
    default boolean existsById(ID id){
        return this.getRepository().existsById(id);
    }

    /**
     * @see JpaRepository#count() 
     */
    default long count(){
        return this.getRepository().count();
    }

    /**
     * @see JpaRepository#deleteById(ID)
     */
    default void deleteById(ID id){
        this.getRepository().deleteById(id);
    }

    /**
     * @see JpaRepository#delete(Object)
     */
    default void delete(E entity){
        this.getRepository().delete(entity);
    }

    /**
     * @see JpaRepository#deleteAll(Iterable)
     */
    default void deleteAll(Iterable<? extends E> entities){
        this.getRepository().deleteAll(entities);
    }

    /**
     * @see JpaRepository#deleteAll()
     */
    default void deleteAll(){
        this.getRepository().deleteAll();
    }

    /**
     * 通过搜索实体删除数据
     * @param items 搜索实体集合
     */
    default void deleteAll(List<SearchItem> items){
        List<E> entities = this.findAll(items);
        this.deleteAll(entities);
    }

    /**
     * @see JpaRepository#findAll()
     */
    default List<E> findAll(){
        return this.getRepository().findAll();
    }

    /**
     * @see JpaRepository#findAll(Sort)
     */
    default List<E> findAll(Sort sort){
        return this.getRepository().findAll(sort);
    }

    /**
     * @see JpaRepository#findAllById(Iterable)
     */
    default List<E> findAllById(Iterable<ID> idList){
        return this.getRepository().findAllById(idList);
    }

    /**
     * @see JpaRepository#saveAll(Iterable)
     */
    default List<E> saveAll(Iterable<E> entities){
        return this.getRepository().saveAll(entities);
    }

    /**
     * @see JpaRepository#saveAndFlush(Object)
     */
    default E saveAndFlush(E entity){
        return this.getRepository().saveAndFlush(entity);
    }

    /**
     * @see JpaRepository#deleteInBatch(Iterable)
     */
    default void deleteInBatch(Iterable<E> entities){
        this.getRepository().deleteInBatch(entities);
    }

    /**
     * @see JpaRepository#deleteAllInBatch() 
     */
    default void deleteAllInBatch(){
        this.getRepository().deleteAllInBatch();
    }

    /**
     * @see JpaRepository#getOne(ID) 
     */
    default E getOne(ID id){
        return this.getRepository().getOne(id);
    }

    /**
     * @see JpaRepository#findAll(Example)
     */
    default List<E> findAll(Example<E> example){
        return this.getRepository().findAll(example);
    }

    /**
     * @see JpaRepository#findAll(Example, Sort) 
     */
    default List<E> findAll(Example<E> example, Sort sort){
        return this.getRepository().findAll(example,sort);
    }

    /**
     * @see JpaRepository#findAll(Pageable)
     */
    default Page<E> findAll(Pageable pageable){
        return this.getRepository().findAll(pageable);
    }

    /**
     * @see JpaSpecificationExecutor#findOne(Specification) 
     */
    default Optional<E> findOne(@Nullable Specification<E> specification){
        return this.getRepository().findOne(specification);
    }

    /**
     * 查询所有
     * @param items 搜索条件集合
     * @return 实体结果集合
     */
    default List<E> findAll(List<SearchItem> items){
        return this.findAll(((root, query, builder) -> builder.and(
                items.stream()
                        .map(item -> item.getType().getHandler().toPredicate(root, query, builder, item))
                        .toArray(Predicate[]::new)
        )));
    }

    /**
     * 查询所有
     * @param items 搜索条件集合
     * @param pageable 分页参数
     * @return 实体结果分页集合
     */
    default Page<E> findAll(List<SearchItem> items, Pageable pageable){
        return this.findAll(((root, query, builder) -> builder.and(
                items.stream()
                        .map(item -> item.getType().getHandler().toPredicate(root, query, builder, item))
                        .toArray(Predicate[]::new)
        )),pageable);
    }

    /**
     * @see JpaSpecificationExecutor#findAll(Specification) 
     */
    default List<E> findAll(@Nullable Specification<E> specification){
        return this.getRepository().findAll(specification);
    }

    /**
     * @see JpaSpecificationExecutor#findAll(Specification, Pageable) 
     */
    default Page<E> findAll(@Nullable Specification<E> specification, Pageable pageable){
        return this.getRepository().findAll(specification,pageable);
    }

    /**
     * @see JpaSpecificationExecutor#findAll(Specification, Sort)
     */
    default List<E> findAll(@Nullable Specification<E> specification, Sort sort){
        return this.getRepository().findAll(specification,sort);
    }

    /**
     * @see JpaSpecificationExecutor#count(Specification)
     */
    default long count(@Nullable Specification<E> specification){
        return this.getRepository().count(specification);
    }

}
