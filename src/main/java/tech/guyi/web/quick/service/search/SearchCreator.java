package tech.guyi.web.quick.service.search;

import org.springframework.context.ApplicationContext;
import tech.guyi.web.quick.core.utils.TypeConverterUtils;
import tech.guyi.web.quick.service.search.entry.SearchItem;
import tech.guyi.web.quick.service.search.entry.SearchType;
import tech.guyi.web.quick.service.search.getter.SearchValue;
import tech.guyi.web.quick.service.service.QuickService;
import tech.guyi.web.quick.service.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author guyi
 * 搜索实体创建器
 */
public class SearchCreator {

    @Resource
    private ApplicationContext context;

    private Map<String, QuickService> services;

    @PostConstruct
    public void onApplicationStart(){
        this.services = this.context.getBeansOfType(QuickService.class)
                .values()
                .stream()
                .collect(Collectors.toMap(s -> s.entityClass().getSimpleName(), s -> s));
    }


    /**
     * 表达式正则匹配
     */
    private final Pattern patternEX = Pattern.compile("(\\$\\{([a-zA-Z]+)\\.([a-zA-Z]+)\\})");


    /**
     * 通过表达式获取值
     * @param ex 表达式
     * @return 返回值
     */
    private Optional<List<Object>> fromEx(String ex){
        Matcher matcher = patternEX.matcher(ex);
        if (matcher.find()){
            String entityName = matcher.group(2);
            String fieldName = matcher.group(3);
            return Optional.ofNullable(services.get(entityName))
                    .map(QuickService::findAll)
                    .map(list -> (List<Object>) list)
                    .map(stream -> stream
                            .stream()
                            .map(entity -> ReflectionUtils.getFieldValue(fieldName,entity))
                            .collect(Collectors.toList()));
        }
        return Optional.empty();
    }

    /**
     * 根据键值对创建
     * @param classes 实体的Class
     * @param key 键, 形如 name  name.type
     * @param values 值
     * @return 搜索实体
     */
    public SearchItem from(Class<?> classes,String key,List<Object> values){
        if (key.startsWith(".") || key.endsWith(".")){
            return null;
        }

        String[] arr = key.split("\\.");
        SearchType type = arr.length == 1 ? SearchType.EQUALS : SearchType.getByValue(arr[1]);

        if (values.size() == 1 && values.get(0) instanceof String && ((String) values.get(0)).contains(",")){
            values = this.fromEx((String) values.get(0)).orElse(Arrays.asList(((String) values.get(0)).split(",")));
        }

        Optional<SearchValue> searchValue = ReflectionUtils.getAnnotationInField(SearchValue.class,classes,arr[0]);
        if (searchValue.isPresent()){
            values = context.getBean(searchValue.get().value()).handle(values);
        }

        Class<?> fieldType = ReflectionUtils.getFieldType(arr[0],classes);
        List<Object> valuesTmp = values.stream()
                .map(value -> TypeConverterUtils.convert(fieldType,value.toString()))
                .collect(Collectors.toList());

        return new SearchItem(arr[0],type,valuesTmp);
    }

    /**
     * 从集合中创建
     * @param classes 实体的Class
     * @param map 条件集合
     * @return 搜索实体集合
     */
    public List<SearchItem> from(Class<?> classes,Map<String,List<Object>> map){
        return map.entrySet().stream()
                .map(e -> this.from(classes,e.getKey(),e.getValue()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 从请求中创建
     * 从请求体及参数中获取条件
     * 当存在相同条件时, 优先使用参数中的条件
     * @param classes 实体的Class
     * @param request Http请求
     * @return 搜索实体集合
     */
    public List<SearchItem> from(Class<?> classes, HttpServletRequest request){
        Map<String,List<Object>> map = new HashMap<>();
        List<String> fieldNames = ReflectionUtils.getFieldNames(classes);

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            if (fieldNames.stream().anyMatch(name::startsWith)){
                List<Object> list = new LinkedList<>(Collections.singleton(request.getHeader(name)));
                map.put(name, list);
            }
        }

        names = request.getParameterNames();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            if (fieldNames.stream().anyMatch(name::startsWith)){
                List<Object> list = new LinkedList<>(Arrays.asList(request.getParameterValues(name)));
                map.put(name, list);
            }
        }
        return this.from(classes,map);
    }

}
