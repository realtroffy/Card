package by.fin.card.mapper;

import by.fin.card.dto.ProductDto;
import by.fin.card.dto.ProductDtoWithoutFeatureAndPhoto;
import by.fin.card.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDto toDto(Product product);

  Product toEntityWithoutFeatureAndPhoto(ProductDtoWithoutFeatureAndPhoto productDto);
}
