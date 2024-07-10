package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorTransaction {

    @Id @GeneratedValue
    private Long recId;

    @ManyToOne
    @JoinColumn(name = "vend_id",referencedColumnName = "id")
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;

    private Long quantityPurchased;
    private BigDecimal costPrice;


    private String invoiceNumber;

    public VendorTransaction(String invoiceNumber,Vendor vendor, Product product, Long quantityPurchased, BigDecimal costPrice) {
        this.vendor = vendor;
        this.product = product;
        this.quantityPurchased = quantityPurchased;
        this.costPrice = costPrice;
        this.invoiceNumber = invoiceNumber;
    }
}
