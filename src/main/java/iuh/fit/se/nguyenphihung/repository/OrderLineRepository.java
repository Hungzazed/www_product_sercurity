package iuh.fit.se.nguyenphihung.repository;

import iuh.fit.se.nguyenphihung.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findByOrderId(Integer orderId);
    List<OrderLine> findByProductId(Integer productId);
}

