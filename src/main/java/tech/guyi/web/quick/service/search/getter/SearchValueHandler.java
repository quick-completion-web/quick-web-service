package tech.guyi.web.quick.service.search.getter;

import java.util.List;

/**
 * @author guyi
 * 搜索值处理器
 * 用户处理及转换搜索项的值
 */
public interface SearchValueHandler {

    /**
     * 值处理
     * @param origins 原始值
     * @return 处理后的值
     */
    List<Object> handle(List<Object> origins);

}
