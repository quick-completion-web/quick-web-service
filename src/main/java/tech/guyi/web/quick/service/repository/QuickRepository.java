package tech.guyi.web.quick.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author guyi
 * 基础Repository
 * @param <E> 实体类型
 * @param <ID> 实体ID类型
 */
public interface QuickRepository<E,ID> extends JpaRepository<E,ID>, JpaSpecificationExecutor<E> {
}
