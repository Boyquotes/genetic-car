package fr.genetic.server.web.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Validator<T> {

    private final T object;
    private final List<String> messages;

    private Validator(T object) {
        this.object = object;
        this.messages = new ArrayList<>();
    }

    public static <T> Validator<T> of (T object) {
        return new Validator<>(Objects.requireNonNull(object));
    }

    private Validator<T> validate(Predicate<T> validation, Function<T, String> message) {
        if (!validation.test(object)) {
            messages.add(message.apply(object));
        }
        return this;
    }

    public Validator<T> validate(Predicate<T> validation, String message) {
        return validate(validation, t -> message);
    }

    public <U> Validator<T> validate(Function<T,U> projection, Predicate<U> validation, String message) {
        return validate(projection.andThen(validation::test)::apply, t -> message);
    }

    public <U> Validator<T> validate(Function<T,List<U>> projection, int index, Predicate<U> validation, String message) {
        return validate(projection.andThen(us -> validation.test(us.get(index)))::apply,
                projection.andThen(us -> us.get(index)).andThen(coord -> message + " (" + coord + ")"));
    }

    public T throwIfAny() throws IllegalStateException {
        if (messages.isEmpty()) {
            return object;
        }
        String message = messages.stream().collect(Collectors.joining(","));
        throw new IllegalStateException(message);
    }

    public Stream<String> messagesStream() {
        return messages.stream();
    }
}
