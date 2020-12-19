package tech.guyi.web.quick.service.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface QuickServicePageable {

    /**
     * 分页查询下, 默认的每页数据条数
     * @return 每页数据条数
     */
    default int getDefaultSize(){
        return 30;
    }

    default String getDefaultOrder(){
        return "createTime";
    }

    default Sort.Direction getDefaultSort(){
        return Sort.Direction.DESC;
    }

    /**
     * 获取分页请求
     * @param page 请求页码
     * @param size 每页显示条数
     * @param order 排序方式 asc-正序 desc-倒序
     * @param sort 排序字段
     * @return 分页请求
     */
    default Pageable getPageable(Integer page, Integer size, String order, String sort){
        return PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(getDefaultSize()),
                Sort.by(
                        Optional.ofNullable(sort)
                                .map(String::toUpperCase)
                                .map(Sort.Direction::valueOf)
                                .orElse(getDefaultSort()),
                        Optional.ofNullable(order).orElse(getDefaultOrder())
                )
        );
    }

}
