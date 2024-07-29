package by.fin.card.dto;

import java.util.List;

public record ProductDto(Long id,
                         String name,
                         String brand,
                         String model,
                         Integer availableUnits,
                         String weight,
                         Double rating,
                         String color,
                         Double price,
                         String description,
                         String categoryName,
                         List<PhotoDto>photos,
                         List<String> specialFeatures) {
}
