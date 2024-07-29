package by.fin.card.dto;

public record ProductDtoWithoutFeatureAndPhoto(Long id,
                                               String name,
                                               String brand,
                                               String model,
                                               Integer availableUnits,
                                               String weight,
                                               Double rating,
                                               String color,
                                               Double price,
                                               String description,
                                               String categoryName) {
}

