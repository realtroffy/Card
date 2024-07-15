package by.fin.card.service.impl;

import by.fin.card.dto.FeatureDto;
import by.fin.card.dto.ProductDto;
import by.fin.card.dto.ProductDtoWithoutFeatureAndPhoto;
import by.fin.card.mapper.ProductMapper;
import by.fin.card.model.Photo;
import by.fin.card.model.Product;
import by.fin.card.repository.ProductRepository;
import by.fin.card.service.PhotoService;
import by.fin.card.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  public static final String NO_SUCH_PRODUCT_EXCEPTION_MESSAGE = "Product was not found by id = ";
  public static final String NO_PRODUCT_FIND_EXCEPTION_MESSAGE = "Such product was not found";

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final PhotoService photoService;

  @Transactional(readOnly = true)
  @Override
  public List<ProductDto> getAllProducts() {
    List<ProductDto> productDtoList = new ArrayList<>();
    productRepository
        .findAll()
        .forEach(product -> productDtoList.add(productMapper.toDto(product)));
    return productDtoList;
  }

  @Transactional(readOnly = true)
  @Override
  public ProductDto getProductById(Long id) {
    return productMapper.toDto(getProduct(id));
  }

  private Product getProduct(Long id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException(NO_SUCH_PRODUCT_EXCEPTION_MESSAGE + id));
  }

  @Transactional
  @Override
  public ProductDto createProduct(ProductDtoWithoutFeatureAndPhoto productDto) {
    Product product = productMapper.toEntityWithoutFeatureAndPhoto(productDto);
    product.setDescription(generateDescription(product));
    if(product.getRating()==null){
      product.setRating(0d);
    }
    if(product.getAvailableUnits()==null){
      product.setAvailableUnits(0);
    }
    return productMapper.toDto(productRepository.save(product));
  }

  @Transactional
  @Override
  public ProductDto updateProduct(Long id, ProductDtoWithoutFeatureAndPhoto productDto) {
    Product product = getProduct(id);
    if (productDto.getName() != null) {
      product.setName(productDto.getName());
    }
    if (productDto.getBrand() != null) {
      product.setBrand(productDto.getBrand());
    }
    if (productDto.getModel() != null) {
      product.setModel(productDto.getModel());
    }
    if (productDto.getAvailableUnits() != null) {
      product.setAvailableUnits(productDto.getAvailableUnits());
    }
    if (productDto.getWeight() != null) {
      product.setWeight(productDto.getWeight());
    }
    if (productDto.getRating() != null) {
      product.setRating(productDto.getRating());
    }
    if (productDto.getCategoryName() != null) {
      product.setCategoryName(productDto.getCategoryName());
    }
    if (productDto.getColor() != null) {
      product.setColor(productDto.getColor());
    }
    if (productDto.getPrice() != null) {
      product.setPrice(productDto.getPrice());
    }
    if (productDto.getDescription() != null) {
      product.setDescription(generateDescription(product));
    }
    Product savedProduct = productRepository.save(product);
    return productMapper.toDto(savedProduct);
  }

  @Transactional
  @Override
  public ProductDto deleteProduct(Long id) {
    Product productById = getProduct(id);
    productRepository.deleteById(id);
    return productMapper.toDto(productById);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductDto getProductWithHighestRating() {
    Product product =
        productRepository.findAll().stream()
            .max(Comparator.comparingDouble(Product::getRating))
            .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FIND_EXCEPTION_MESSAGE));
    return productMapper.toDto(product);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductDto getMostExpensiveProduct() {
    Product product =
        productRepository.findAll().stream()
            .max(Comparator.comparingDouble(Product::getPrice))
            .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FIND_EXCEPTION_MESSAGE));
    return productMapper.toDto(product);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductDto getCheapestProduct() {
    Product product =
        productRepository.findAll().stream()
            .min(Comparator.comparingDouble(Product::getPrice))
            .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FIND_EXCEPTION_MESSAGE));
    return productMapper.toDto(product);
  }

  private String generateDescription(Product product) {
    String featureNames = "";
    if (product.getSpecialFeatures() != null) {
      featureNames = String.join(", ", product.getSpecialFeatures());
    }
    return String.format(
        "Прекрасные %s %s, цвет: %s. %s",
        product.getName(), product.getBrand(), product.getColor(), featureNames);
  }

  @Override
  @Transactional
  public ProductDto addSpecialFeature(Long id, FeatureDto featureDto) {
    Product product = getProduct(id);
    product.addSpecialFeature(featureDto.getFeatureName());
    product.setDescription(generateDescription(product));
    productRepository.save(product);
    return productMapper.toDto(product);
  }

  @Override
  @Transactional
  public ProductDto removeSpecialFeature(Long id, FeatureDto featureDto) {
    Product product = getProduct(id);
    product.removeSpecialFeature(featureDto.getFeatureName());
    product.setDescription(generateDescription(product));
    productRepository.save(product);
    return productMapper.toDto(product);
  }

  @Override
  @Transactional
  public ProductDto addPhotoToProduct(Long productId, Long photoId) {
    Product product = getProduct(productId);
    Photo photo = photoService.getPhoto(photoId);
    product.addPhoto(photo);
    return productMapper.toDto(product);
  }
}
