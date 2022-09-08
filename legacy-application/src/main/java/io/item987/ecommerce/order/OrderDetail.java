package io.item987.ecommerce.order;

import jakarta.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(nullable = false)
    private Integer quantity;

    public OrderDetail() {}

    public OrderDetail(Order order, long productId, Integer quantity) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
