package by.fin.card.service;

import by.fin.card.dto.FeatureDto;
import by.fin.card.dto.ProductDto;
import by.fin.card.dto.ProductDtoWithoutFeatureAndPhoto;

import java.util.List;

public interface ProductService {

  List<ProductDto> getAllProducts();

  ProductDto getProductById(Long id);

  ProductDto createProduct(ProductDtoWithoutFeatureAndPhoto productDto);

  ProductDto updateProduct(Long id, ProductDtoWithoutFeatureAndPhoto productDto);

  ProductDto deleteProduct(Long id);

  ProductDto getProductWithHighestRating();

  ProductDto getMostExpensiveProduct();

  ProductDto getCheapestProduct();

  ProductDto addSpecialFeature(Long id, FeatureDto featureDto);

  ProductDto removeSpecialFeature(Long id, FeatureDto featureDto);

  ProductDto addPhotoToProduct(Long productId, Long photoId);
}
