package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String features;
/*
    @Enumerated(EnumType.STRING)
    private Status status;

 */

   private Long quantity;
   private BigDecimal regularPrice;
   private BigDecimal salePrice;

    @Transient
    private List<String> images = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(mappedBy = "products")
    private List<Category> categories = new ArrayList<>();

    /*
    @JsonManagedReference
    @ManyToMany(mappedBy = "products")
    private Set<Attributes> attributes= new HashSet<>();
*/


    public Product(String name, String description,String features, Long quantity, BigDecimal regularPrice,
                   BigDecimal salePrice) {
        this.name = name;
        this.description = description;
        this.features = features;
        this.quantity = quantity;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
    }

    public Product(String name, String description,String features, Long quantity, BigDecimal regularPrice, BigDecimal salePrice, List<String> images,
                   List<Category> categories) {
        this.name = name;
        this.description = description;
        this.features = features;
        this.quantity = quantity;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.images = images;
        this.categories = categories;
    }

}
