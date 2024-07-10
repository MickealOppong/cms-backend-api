package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItem extends LogEntity {


    @Id @GeneratedValue
    private Long orderId;
    private String name;
    private String description;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "fk_order",referencedColumnName = "recId")
    private Order order;
    public OrderLineItem(String name, String description, BigDecimal regularPrice,
                         BigDecimal salePrice, Long quantity) {
        this.name = name;
        this.description = description;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.quantity = quantity;
    }
}
