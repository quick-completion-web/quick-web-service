package tech.guyi.web.quick.service.controller;

import tech.guyi.web.quick.core.controller.interfaces.QuickUpdateController;
import tech.guyi.web.quick.core.controller.interfaces.entry.QuickRequestEntity;
import tech.guyi.web.quick.service.controller.handler.ServiceUpdateEntityHandler;

/**
 * @author guyi
 * 实体更新控制器
 * @param <E> 实体类型
 */
public interface QuickServiceUpdateController<E extends QuickRequestEntity<ID>, ID> extends QuickUpdateController<E,ID>, ServiceUpdateEntityHandler<E,ID> {

}
