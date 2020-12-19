package tech.guyi.web.quick.service.search.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author guyi
 * 搜索类型
 */
@Getter
@AllArgsConstructor
public enum SearchType {

    EQUALS("equals", "匹配", new SearchBuilder() {
        @Override
        public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item) {
            return Optional.of(item.getValues()
                    .stream()
                    .filter(value -> !StringUtils.isEmpty(value))
                    .map(value -> builder.equal(root.get(item.getField()), value))
                    .toArray(Predicate[]::new))
                    .filter(predicates -> predicates.length > 0)
                    .map(builder::or)
                    .orElse(null);
        }
    }),
    LIKE("like", "模糊匹配", new SearchBuilder() {
        @Override
        public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item) {
            return Optional.of(item.getValues()
                    .stream()
                    .filter(value -> !StringUtils.isEmpty(value))
                    .map(value -> builder.like(root.get(item.getField()), value.toString()))
                    .toArray(Predicate[]::new))
                    .filter(predicates -> predicates.length > 0)
                    .map(builder::or)
                    .orElse(null);
        }
    }),
    IN("in", "包含", new SearchBuilder() {
        @Override
        public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item) {
            CriteriaBuilder.In<Object> in = builder.in(root.get(item.getField()));
            item.getValues().forEach(in::value);
            return in;
        }
    }),
    BETWEEN("between", "区间", new SearchBuilder() {
        @Override
        public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item) {
            return builder.between(root.get(item.getField()),(Long) item.getValues().get(0),(Long) item.getValues().get(1));
        }
    }),
    NULL("null", "包含", new SearchBuilder() {
        @Override
        public <T> Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchItem item) {
            return item.getValues().stream().findFirst()
                    .map(Object::toString)
                    .map(Boolean::valueOf)
                    .filter(value -> value)
                    .map(value -> builder.isNull(root.get(item.getField())))
                    .orElse(builder.isNotNull(root.get(item.getField())));
        }
    });

    private final String value;
    private final String text;
    private final SearchBuilder handler;

    public static SearchType getByValue(String value){
        return Arrays.stream(values())
                .filter(t -> t.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
