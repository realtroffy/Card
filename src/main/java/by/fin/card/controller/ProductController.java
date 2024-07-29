package by.fin.card.controller;

import by.fin.card.dto.FeatureDto;
import by.fin.card.dto.ProductDto;
import by.fin.card.dto.ProductDtoWithoutFeatureAndPhoto;
import by.fin.card.dto.ProductPageDto;
import by.fin.card.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ResponseEntity<ProductPageDto> getAllProducts() {
    List<ProductDto> productList = productService.getAllProducts();
    ProductPageDto productPageDto = new ProductPageDto(productList);
    return ResponseEntity.ok(productPageDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDtoWithoutFeatureAndPhoto productDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(
      @PathVariable Long id, @RequestBody ProductDtoWithoutFeatureAndPhoto productDto) {
    return ResponseEntity.ok(productService.updateProduct(id, productDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.deleteProduct(id));
  }

  @GetMapping("/highest-rated")
  public ResponseEntity<ProductDto> getProductWithHighestRating() {
    return ResponseEntity.ok(productService.getProductWithHighestRating());
  }

  @GetMapping("/most-expensive")
  public ResponseEntity<ProductDto> getMostExpensiveProduct() {
    return ResponseEntity.ok(productService.getMostExpensiveProduct());
  }

  @GetMapping("/cheapest")
  public ResponseEntity<ProductDto> getCheapestProduct() {
    return ResponseEntity.ok(productService.getCheapestProduct());
  }

  @PutMapping("/features/{id}")
  public ResponseEntity<ProductDto> addFeatureToProduct(
      @PathVariable Long id, @RequestBody FeatureDto featureDto) {
    return ResponseEntity.ok(productService.addSpecialFeature(id, featureDto));
  }

  @DeleteMapping("/features/{id}")
  public ResponseEntity<ProductDto> removeFeatureFromProduct(
      @PathVariable Long id, @RequestBody FeatureDto featureDto) {
    return ResponseEntity.ok(productService.removeSpecialFeature(id, featureDto));
  }

  @PutMapping("/{productId}/photos/{photoId}")
  public ResponseEntity<ProductDto> addPhotoToProduct(
          @PathVariable Long productId, @PathVariable Long photoId) {
    return ResponseEntity.ok(productService.addPhotoToProduct(productId, photoId));
  }
}
