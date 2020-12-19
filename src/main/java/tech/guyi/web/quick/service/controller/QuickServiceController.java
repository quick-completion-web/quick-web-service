package tech.guyi.web.quick.service.controller;

import tech.guyi.web.quick.core.controller.interfaces.entry.QuickRequestEntity;

public interface QuickServiceController<E extends QuickRequestEntity<ID>, ID> extends QuickServiceUpdateController<E,ID>, QuickServiceQueryController<E,ID> {

}
