package io.item987.ecommerce.product;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Set;

final class IgnoredExceptions {

    private IgnoredExceptions() {}

    private static final Set<Class<? extends Throwable>> ignoredExceptions = Set.of(
            WebClientResponseException.BadRequest.class,
            WebClientResponseException.Unauthorized.class,
            WebClientResponseException.Forbidden.class,
            WebClientResponseException.NotFound.class,
            WebClientResponseException.MethodNotAllowed.class,
            WebClientResponseException.NotAcceptable.class,
            WebClientResponseException.Gone.class,
            WebClientResponseException.NotImplemented.class,
            WebClientResponseException.UnsupportedMediaType.class,
            WebClientResponseException.UnprocessableEntity.class
    );

    static boolean isIgnored(Class<? extends Throwable> exception) {
        return ignoredExceptions.contains(exception);
    }

    @SuppressWarnings("unchecked")
    static Class<? extends Throwable>[] getArray() {
        return ignoredExceptions.toArray(Class[]::new);
    }

}
