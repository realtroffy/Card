package by.fin.card.repository;

import by.fin.card.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductAllRepository {
}
