package iuh.fit.se.nguyenphihung.repository;

import iuh.fit.se.nguyenphihung.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerByNameIs(String name);
    List<Customer> findByNameContainingIgnoreCase(String name);
}

