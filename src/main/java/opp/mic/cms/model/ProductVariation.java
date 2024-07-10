package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariation extends LogEntity {

    @Id @GeneratedValue
    private Long recId;
    private String description;
    private String skuValue;
    private Long quantity;
    private BigDecimal price;
    private String name;

    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "id")
    private Product product;



    public ProductVariation(String name,String description, String skuValue, Long quantity, BigDecimal price,
                            Product product) {
        this.description = description;
        this.skuValue = skuValue;
        this.quantity = quantity;
        this.price = price;
      this.name = name;
        this.product = product;
    }
}
