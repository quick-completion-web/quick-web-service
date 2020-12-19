package tech.guyi.web.quick.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.guyi.web.quick.service.datasource.DataSourceGetter;
import tech.guyi.web.quick.service.search.SearchCreator;

@Configuration
public class AutoConfiguration {

    @Bean
    public SearchCreator searchCreator(){
        return new SearchCreator();
    }

    @Bean
    public DataSourceGetter dataSourceGetter(){
        return new DataSourceGetter();
    }
}
