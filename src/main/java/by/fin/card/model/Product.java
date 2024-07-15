package by.fin.card.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product")
public class Product implements Serializable {

  @Serial private static final long serialVersionUID = 4603749101621919833L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String brand;
  private String model;
  private Integer availableUnits;
  private String weight;
  private Double rating;
  private String categoryName;
  private String description;
  private String color;
  private Double price;

  @ElementCollection
  @CollectionTable(
      name = "product_special_features",
      joinColumns = @JoinColumn(name = "product_id"))
  @Column(name = "special_feature")
  private List<String> specialFeatures = new ArrayList<>();

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  private List<Photo> photos = new ArrayList<>();

  public void addSpecialFeature(String feature) {
    specialFeatures.add(feature);
  }

  public void removeSpecialFeature(String feature) {
    specialFeatures.remove(feature);
  }

  public void addPhoto(Photo photo) {
    photos.add(photo);
    photo.setProduct(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Double.compare(product.price, price) == 0
        && Objects.equals(id, product.id)
        && Objects.equals(name, product.name)
        && Objects.equals(brand, product.brand)
        && Objects.equals(model, product.model)
        && Objects.equals(color, product.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, brand, model, color, price);
  }
}
