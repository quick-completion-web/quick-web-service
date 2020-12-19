package tech.guyi.web.quick.service.search.getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author guyi
 * 通过此注解指定字段所使用的搜索值处理器
 * 需要保证指定的处理器再Spring容器中
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchValue {

    Class<? extends SearchValueHandler> value();

}
