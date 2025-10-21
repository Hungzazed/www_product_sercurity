package iuh.fit.se.nguyenphihung.repository;

import iuh.fit.se.nguyenphihung.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}

