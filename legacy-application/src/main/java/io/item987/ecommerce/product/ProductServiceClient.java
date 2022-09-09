package io.item987.ecommerce.product;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

    private static final String SERVICE_API_URL = "http://localhost:8081/api/products";
    private static final String SERVICE_ID = "productService";

    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(5);

    private static final ParameterizedTypeReference<ProductData> productDataTypeRef = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<ProductData>> productDataListTypeRef = new ParameterizedTypeReference<>() {};

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;
    private final RetryRegistry retryRegistry;
    private final RetryConfig retryConfig;

    public ProductServiceClient(WebClient.Builder webClientBuilder, CircuitBreakerFactory<?, ?> circuitBreakerFactory,
                                RetryRegistry retryRegistry, RetryConfig retryConfig) {
        this.webClient = webClientBuilder.baseUrl(SERVICE_API_URL).build();
        this.circuitBreaker = circuitBreakerFactory.create(SERVICE_ID);
        this.retryRegistry = retryRegistry;
        this.retryConfig = retryConfig;
    }

    public boolean isProductPresent(long productId) {
        try {
            getProduct(productId);
            return true;
        }
        catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == HttpStatus.NOT_FOUND.value())
                return false;
            else
                throw e;
        }
    }

    public List<ProductData> getProducts(List<Long> ids) {
        var joinedIds = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        return doRequest(webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("ids", joinedIds).build()), productDataListTypeRef);
    }

    public ProductData getProduct(long id) {
        return doRequest(webClient.get().uri("/{id}", id), productDataTypeRef);
    }

    public ProductData createProduct(ProductData productData) {
        return doRequest(webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productData)), productDataTypeRef);
    }

    public ProductData updateProduct(ProductData productData) {
        return doRequest(webClient.put()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productData)), productDataTypeRef);
    }

    public void deleteProduct(long id) {
        doRequest(webClient.delete().uri("/{id}", id), new ParameterizedTypeReference<Void>() {});
    }

    private <T> T doRequest(WebClient.RequestHeadersSpec<?> requestSpec, ParameterizedTypeReference<T> bodyTypeRef) {
        var retry = retryRegistry.retry(SERVICE_ID, retryConfig);
        var responseSupplier = Retry.decorateSupplier(retry,
                () -> requestSpec.retrieve().bodyToMono(bodyTypeRef).block(BLOCK_TIMEOUT));

        return circuitBreaker.run(responseSupplier, this::doFallback);
    }

    private <T> T doFallback(Throwable throwable) {
        logger.warn("Circuit breaker fallback triggered", throwable);

        if (IgnoredExceptions.isIgnored(throwable.getClass())) {
            if (throwable instanceof RuntimeException runtimeException)
                throw runtimeException;
            else
                throw new RuntimeException(throwable);
        }
        else {
            throw new ProductServiceUnavailableException();
        }
    }

}
