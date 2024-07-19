package by.fin.card.repository;

import by.fin.card.model.Product;

import java.util.List;

public interface ProductAllRepository {

    List<Product> findAllProductWithFeatureAndPhoto();
}
