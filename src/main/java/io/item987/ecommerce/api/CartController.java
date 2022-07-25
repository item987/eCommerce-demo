package io.item987.ecommerce.api;

import io.item987.ecommerce.api.dto.Cart;
import io.item987.ecommerce.api.dto.CartItem;
import io.item987.ecommerce.order.OrderDetail;
import io.item987.ecommerce.order.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static io.item987.ecommerce.api.Constants.API_URL_CART;

@RestController
@RequestMapping(API_URL_CART)
public class CartController {

    private final OrderService orderService;

    public CartController(OrderService orderService) {
        this.orderService = orderService;
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

    private static Cart createCart(Collection<OrderDetail> orderDetails) {
        return new Cart(orderDetails.stream().map(CartItem::new).toList());
    }

}
