package by.fin.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ProductDtoWithoutFeatureAndPhoto {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private Integer availableUnits;
    private String weight;
    private Double rating;
    private String color;
    private Double price;
    private String description;
    private String categoryName;
}
