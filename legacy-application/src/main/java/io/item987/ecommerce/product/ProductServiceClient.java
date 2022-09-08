package io.item987.ecommerce.product;

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

    private static final String SERVICE_API_URL = "http://localhost:8081/api/products";
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(5);

    private static final ParameterizedTypeReference<ProductData> productDataTypeRef = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<List<ProductData>> productDataListTypeRef = new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public ProductServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(SERVICE_API_URL).build();
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

    private static <T> T doRequest(WebClient.RequestHeadersSpec<?> requestSpec,
                                   ParameterizedTypeReference<T> bodyTypeRef) {
        return requestSpec.retrieve().bodyToMono(bodyTypeRef).block(BLOCK_TIMEOUT);
    }

}
