package tech.guyi.web.quick.service.datasource;

import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

public class DataSourceGetter {

    @Resource
    private ApplicationContext context;
    @Resource
    private DataSource dataSource;

    public DataSource get(String name){
        return this.context.getBeansOfType(DataSource.class).entrySet()
                .stream()
                .filter(e -> e.getKey().equals(name))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(this.dataSource);
    }

    public Collection<DataSource> all(){
        return this.context.getBeansOfType(DataSource.class).values();
    }

}
