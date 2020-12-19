package tech.guyi.web.quick.service.search.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author guyi
 * 搜索实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchItem {

    private String field;
    private SearchType type;
    private List<Object> values;

}
