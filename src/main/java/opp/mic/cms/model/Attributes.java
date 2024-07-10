package opp.mic.cms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attributes extends LogEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;


    /*
    @JsonManagedReference
    @OneToMany(mappedBy = "attributes",fetch = FetchType.EAGER)
    private List<ProductSKU> productSKU = new ArrayList<>();

     */

   @Transient
    private List<ProductSKU> productSKU = new ArrayList<>();

    /*
    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "product_attributes",
            joinColumns=@JoinColumn(name = "attribute_id",referencedColumnName = "id") ,
            inverseJoinColumns=@JoinColumn(name = "product_id",referencedColumnName = "id"))
    private List<Product> products = new ArrayList<>();

     */

    public Attributes(String name) {
        this.name = name;
    }

    public Attributes(String name,List<ProductSKU> productSKU) {
        this.name = name;
        this.productSKU = productSKU;
    }


}
