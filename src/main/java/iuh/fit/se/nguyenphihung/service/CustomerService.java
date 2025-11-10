package iuh.fit.se.nguyenphihung.service;

import iuh.fit.se.nguyenphihung.entities.Customer;
import iuh.fit.se.nguyenphihung.entities.ROLE;
import iuh.fit.se.nguyenphihung.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;

    public Customer findByUsername(String name) {
        return customerRepository.findCustomerByNameIs(name);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> searchByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    public boolean existsByUsername(String username) {
        return customerRepository.findCustomerByNameIs(username) != null;
    }

    public Customer registerNewCustomer(String username, String password) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Customer customer = new Customer();
        customer.setName(username);
        customer.setPassword(new BCryptPasswordEncoder().encode(password));
        customer.setRoles(List.of(ROLE.ROLE_CUSTOMER));
        customer.setCustomerSince(Calendar.getInstance());

        return customerRepository.save(customer);
    }
}
