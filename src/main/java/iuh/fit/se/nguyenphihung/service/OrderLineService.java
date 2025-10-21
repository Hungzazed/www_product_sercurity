package iuh.fit.se.nguyenphihung.service;

import iuh.fit.se.nguyenphihung.entities.OrderLine;
import iuh.fit.se.nguyenphihung.repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderLineService {
    @Autowired
    private OrderLineRepository orderLineRepository;

    public List<OrderLine> findAll() {
        return orderLineRepository.findAll();
    }

    public Optional<OrderLine> findById(Integer id) {
        return orderLineRepository.findById(id);
    }

    public OrderLine save(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    public void deleteById(Integer id) {
        orderLineRepository.deleteById(id);
    }

    public List<OrderLine> findByOrderId(Integer orderId) {
        return orderLineRepository.findByOrderId(orderId);
    }

    public List<OrderLine> findByProductId(Integer productId) {
        return orderLineRepository.findByProductId(productId);
    }
}

