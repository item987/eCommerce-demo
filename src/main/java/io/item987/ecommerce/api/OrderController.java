package io.item987.ecommerce.api;

import io.item987.ecommerce.api.dto.OrderData;
import io.item987.ecommerce.order.OrderService;
import io.item987.ecommerce.order.OrderStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.item987.ecommerce.api.Constants.API_URL_ORDERS;

@RestController
@RequestMapping(API_URL_ORDERS)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderData> getAllOrders() {
        return orderService.getAllOrders().stream().map(OrderData::new).toList();
    }

    @GetMapping("/new")
    public List<OrderData> getNewOrders() {
        return orderService.getOrders(OrderStatus.NEW).stream().map(OrderData::new).toList();
    }

    @GetMapping("/completed")
    public List<OrderData> getCompletedOrders() {
        return orderService.getOrders(OrderStatus.COMPLETED).stream().map(OrderData::new).toList();
    }

    @GetMapping("/{id}")
    public OrderData getOrder(@PathVariable long id) {
        return new OrderData(orderService.getOrder(id));
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
    }

}
