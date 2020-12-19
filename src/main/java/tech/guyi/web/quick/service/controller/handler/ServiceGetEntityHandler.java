package tech.guyi.web.quick.service.controller.handler;

import org.springframework.util.StringUtils;
import tech.guyi.web.quick.core.controller.interfaces.handler.GetEntityHandler;
import tech.guyi.web.quick.service.getter.GetService;

import java.util.List;
import java.util.Optional;

public interface ServiceGetEntityHandler<E,ID> extends GetEntityHandler<E,ID>, GetService<E,ID> {

    @Override
    default Optional<E> getById(ID id) {
        return this.getService().findById(id);
    }

    @Override
    default boolean existsById(ID id) {
        return !StringUtils.isEmpty(id) && this.getService().existsById(id);
    }

    @Override
    default List<E> findAll(){
        return this.getService().findAll();
    }
}
