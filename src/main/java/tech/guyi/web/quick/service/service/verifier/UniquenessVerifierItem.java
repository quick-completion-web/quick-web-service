package tech.guyi.web.quick.service.service.verifier;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
public class UniquenessVerifierItem<E> {

    private final String message;
    private final UniquenessVerifier<E> verifier;

    private final String fieldName;
    private final Function<E,Object> supplier;

    public UniquenessVerifierItem(String message, UniquenessVerifier<E> verifier) {
        this.message = message;
        this.verifier = verifier;
        this.fieldName = null;
        this.supplier = null;
    }

    public UniquenessVerifierItem(String message, String fieldName, Function<E, Object> supplier) {
        this.message = message;
        this.fieldName = fieldName;
        this.supplier = supplier;
        this.verifier = (root, query, builder, entity) ->
                builder.and(builder.equal(root.get(this.fieldName),this.supplier.apply(entity)));
    }

}
