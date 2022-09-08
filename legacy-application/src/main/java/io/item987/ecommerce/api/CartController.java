package io.item987.ecommerce.api;

import io.item987.ecommerce.api.dto.Cart;
import io.item987.ecommerce.api.dto.CartItem;
import io.item987.ecommerce.order.OrderDetail;
import io.item987.ecommerce.order.OrderService;
import io.item987.ecommerce.product.ProductData;
import io.item987.ecommerce.product.ProductServiceClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.item987.ecommerce.api.Constants.API_URL_CART;

@RestController
@RequestMapping(API_URL_CART)
public class CartController {

    private final OrderService orderService;
    private final ProductServiceClient productServiceClient;

    public CartController(OrderService orderService, ProductServiceClient productServiceClient) {
        this.orderService = orderService;
        this.productServiceClient = productServiceClient;
    }

    @GetMapping
    @Transactional
    public Cart getCart() {
        return createCart(orderService.getCurrentOrderDetails());
    }

    @PutMapping
    public Cart updateCart(@RequestBody Cart cart) {
        return createCart(orderService.updateCurrentOrderDetails(cart));
    }

    @PostMapping("/submit")
    @Transactional
    public void submitCart(@RequestBody Cart cart) {
        updateCart(cart);
        orderService.completeCurrentOrder();
    }

    private Cart createCart(Collection<OrderDetail> orderDetails) {
        var productIds = orderDetails.stream().map(OrderDetail::getProductId).toList();
        var products = productServiceClient.getProducts(productIds).stream()
                .collect(Collectors.toMap(ProductData::id, Function.identity()));

        var cartItems =  orderDetails.stream()
                .map(orderDetail -> createCartItem(orderDetail, products))
                .filter(Objects::nonNull)
                .toList();
        return new Cart(cartItems);
    }

    private static CartItem createCartItem(OrderDetail orderDetail, Map<Long, ProductData> products) {
        var product = products.get(orderDetail.getProductId());
        if (product != null)
            return new CartItem(orderDetail.getId(), product, orderDetail.getQuantity());
        else
            return null;
    }
}
