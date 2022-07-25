package io.item987.ecommerce.order;

import io.item987.ecommerce.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrders(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order getOrder(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("Order not found"));
    }

    private Order getCurrentNewOrder() {
        return orderRepository.findOneByStatus(OrderStatus.NEW).orElseGet(this::createOrder);
    }

    public List<OrderDetail> getCurrentOrderDetails() {
        return orderDetailRepository.findByOrder(getCurrentNewOrder());
    }

    @Transactional
    public List<OrderDetail> updateCurrentOrderDetails(OrderDetailList orderDetailList) {
        var newOrder = getCurrentNewOrder();
        var existingOrderDetails = orderDetailRepository.findByOrderAndMapById(newOrder);

        var detailsToDelete = findDetailsToDelete(orderDetailList.items(), existingOrderDetails.values());
        var detailsToSave = findDetailsToSave(orderDetailList.items(), existingOrderDetails, newOrder);

        orderDetailRepository.deleteAll(detailsToDelete);

        return orderDetailRepository.saveAll(detailsToSave);
    }

    private List<OrderDetail> findDetailsToDelete(List<? extends OrderDetailModel> submittedDetails,
                                                  Collection<OrderDetail> existingDetails) {
        var submittedIds = submittedDetails.stream()
                .map(OrderDetailModel::id)
                .collect(Collectors.toSet());
        return existingDetails.stream()
                .filter(orderDetail -> !submittedIds.contains(orderDetail.getId()))
                .toList();
    }

    private List<OrderDetail> findDetailsToSave(List<? extends OrderDetailModel> submittedDetails,
                                                Map<Long, OrderDetail> existingDetails, Order order) {
        var detailsToSave = new ArrayList<OrderDetail>();

        for (var orderDetailData : submittedDetails) {
            OrderDetail orderDetail;
            var product = productService.getProduct(orderDetailData.productId());
            var quantity = orderDetailData.quantity();
            var id = orderDetailData.id();

            if (id == null) {
                orderDetail = new OrderDetail(order, product, orderDetailData.quantity());
            }
            else {
                orderDetail = existingDetails.get(id);
                if (orderDetail == null)
                    throw new OrderException(String.format("Order detail with id '%d' not found", id));
                orderDetail.setProduct(product);
                orderDetail.setQuantity(quantity);
            }

            detailsToSave.add(orderDetail);
        }

        return detailsToSave;
    }

    public void completeCurrentOrder() {
        var currentOrder = getCurrentNewOrder();
        currentOrder.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(currentOrder);
    }

    private Order createOrder() {
        return orderRepository.save(new Order());
    }

    @Transactional
    public void deleteOrder(long id) {
        orderDetailRepository.deleteByOrder(id);
        orderRepository.deleteById(id);
    }

}
