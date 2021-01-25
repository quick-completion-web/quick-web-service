package tech.guyi.web.quick.service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.service.entity.QuickEntity;
import tech.guyi.web.quick.service.getter.GetService;
import tech.guyi.web.quick.service.search.SearchCreator;

import javax.servlet.http.HttpServletRequest;

public interface QuickServiceSearchController<E,ID> extends GetService<E,ID>,QuickServicePageable {

    SearchCreator getSearchCreator();

    @GetMapping("search")
    default ResponseEntity<ResponseContent<Page<E>>> search(@RequestParam(name = "page", required = false) Integer page,
                                                            @RequestParam(name = "size", required = false) Integer size,
                                                            @RequestParam(name = "order", required = false) String order,
                                                            @RequestParam(name = "sort", required = false) String sort,
                                                            HttpServletRequest request){
        Pageable pageable = this.getPageable(page,size,order,sort);
        return ResponseEntities.ok(
                this.getService().search(
                        this.getSearchCreator().from(this.getService().entityClass(),request),
                        pageable
                )
        );
    }

}
