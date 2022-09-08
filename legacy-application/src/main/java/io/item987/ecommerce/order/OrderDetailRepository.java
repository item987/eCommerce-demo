package io.item987.ecommerce.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrder(Order order);

    default Map<Long, OrderDetail> findByOrderAndMapById(Order order) {
        return findByOrder(order).stream().collect(Collectors.toMap(OrderDetail::getId, Function.identity()));
    }

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM OrderDetail orderDetail WHERE orderDetail.order.id = :orderId")
    void deleteByOrder(long orderId);

}
