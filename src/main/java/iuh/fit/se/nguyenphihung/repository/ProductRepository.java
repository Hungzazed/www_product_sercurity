package iuh.fit.se.nguyenphihung.repository;

import iuh.fit.se.nguyenphihung.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByInStock(Boolean inStock);
    List<Product> findByCategoryId(Integer categoryId);
}

