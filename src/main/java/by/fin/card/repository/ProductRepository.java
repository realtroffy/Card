package by.fin.card.repository;

import by.fin.card.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductAllRepository {

  Optional<Product> findTopByOrderByPriceDesc();

  Optional<Product> findTopByOrderByPriceAsc();

  Optional<Product> findTopByOrderByRatingDesc();
}
