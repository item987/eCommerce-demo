package io.item987.ecommerce.order;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "e_order")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
