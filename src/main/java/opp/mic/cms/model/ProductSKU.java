package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSKU extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String description;
    private String skuValue;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fk_attr",referencedColumnName = "id")
    private Attributes attributes;


    public ProductSKU(Attributes attributes, String description,String skuValue) {
        this.attributes = attributes;
        this.description = description;
        this.skuValue = skuValue;
    }

}
