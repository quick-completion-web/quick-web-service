package tech.guyi.web.quick.service.controller.handler;

import tech.guyi.web.quick.core.controller.interfaces.handler.UpdateEntityHandler;
import tech.guyi.web.quick.service.getter.GetService;

public interface ServiceUpdateEntityHandler<E,ID> extends UpdateEntityHandler<E,ID>, GetService<E,ID> {

    @Override
    default void deleteById(ID id) {
        this.getService().deleteById(id);
    }

    @Override
    default E save(E entity) {
        return this.getService().autoSave(entity);
    }
}
