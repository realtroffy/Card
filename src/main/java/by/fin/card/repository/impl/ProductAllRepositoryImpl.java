package by.fin.card.repository.impl;

import by.fin.card.model.Product;
import by.fin.card.repository.ProductAllRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ProductAllRepositoryImpl implements ProductAllRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Product> findAllProductWithFeatureAndPhoto() {
    EntityGraph<?> graphFeatures = entityManager.getEntityGraph("Product.specialFeatures");
    TypedQuery<Product> queryFeatures =
        entityManager.createQuery("SELECT p FROM Product p", Product.class);
    queryFeatures.setHint("jakarta.persistence.fetchgraph", graphFeatures);
    List<Product> productsWithFeatures = queryFeatures.getResultList();

    EntityGraph<?> graphPhotos = entityManager.getEntityGraph("Product.photos");
    TypedQuery<Product> queryPhotos =
        entityManager.createQuery("SELECT p FROM Product p", Product.class);
    queryPhotos.setHint("jakarta.persistence.fetchgraph", graphPhotos);
    List<Product> productsWithPhotos = queryPhotos.getResultList();

    Map<Long, Product> productMap = new HashMap<>();
    for (Product product : productsWithFeatures) {
      productMap.put(product.getId(), product);
    }

    for (Product product : productsWithPhotos) {
      Product existingProduct = productMap.get(product.getId());
      if (existingProduct != null) {
        existingProduct.setPhotos(product.getPhotos());
      } else {
        productMap.put(product.getId(), product);
      }
    }

    return new ArrayList<>(productMap.values());
  }
}
