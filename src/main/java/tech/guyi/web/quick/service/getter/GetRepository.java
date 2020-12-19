package tech.guyi.web.quick.service.getter;

import tech.guyi.web.quick.service.repository.QuickRepository;

public interface GetRepository<E,ID> {

    QuickRepository<E,ID> getRepository();

}
