package tech.guyi.web.quick.service.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.guyi.web.quick.core.controller.ResponseContent;
import tech.guyi.web.quick.core.controller.ResponseEntities;
import tech.guyi.web.quick.core.controller.interfaces.QuickQueryController;
import tech.guyi.web.quick.core.controller.interfaces.entry.QuickRequestEntity;
import tech.guyi.web.quick.service.controller.handler.ServiceGetEntityHandler;

/**
 * @author guyi
 * 实体查询控制器
 * @param <E> 实体类型
 */
public interface QuickServiceQueryController<E extends QuickRequestEntity<ID>,ID> extends QuickQueryController<E,ID>, ServiceGetEntityHandler<E,ID>, QuickServicePageable {

    /**
     * 分页查询
     * @param page 请求页码, 默认为0
     * @param size 每页显示条数, 默认为30
     * @param order 排序方式, 默认为asc
     * @param sort 排序字段, 默认为createTime
     * @return 实体数据集合
     */
    @GetMapping
    default ResponseEntity<ResponseContent<Page<E>>> page(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "sort", required = false) String sort){
        return ResponseEntities.ok(this.getService().findAll(this.getPageable(page,size,order,sort)));
    }

}
