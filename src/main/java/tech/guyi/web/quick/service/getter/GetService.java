package tech.guyi.web.quick.service.getter;

import tech.guyi.web.quick.service.entity.QuickEntity;
import tech.guyi.web.quick.service.service.QuickService;

public interface GetService<E,ID> {

    QuickService<E,ID> getService();

}
