package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Long quantity;
    private Long sale;

    @Transient
    private String icon;


    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns=@JoinColumn(name = "category_id",referencedColumnName = "id") ,
            inverseJoinColumns=@JoinColumn(name = "product_id",referencedColumnName = "id"))
    private List<Product> products = new ArrayList<>();



    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public Category(String name, String description, Long quantity, Long sale) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.sale = sale;
    }



}
