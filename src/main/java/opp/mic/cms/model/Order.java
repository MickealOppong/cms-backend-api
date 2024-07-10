package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.enums.DeliveryStatus;
import opp.mic.cms.enums.OrderStatus;
import opp.mic.cms.enums.PaymentStatus;
import opp.mic.cms.util.LogEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order extends LogEntity {

        @Id @GeneratedValue
        private Long recId;
        private AppUser appUser;
        private OrderStatus orderStatus;
        private DeliveryStatus deliveryStatus;
        private BigDecimal total;
        private LocalDateTime deliveryDate;
        private PaymentStatus paymentStatus;



        @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
        private List<OrderLineItem> orderLineItem = new ArrayList<>();


    public Order(AppUser appUser, OrderStatus orderStatus, DeliveryStatus deliveryStatus, BigDecimal total,
                 LocalDateTime deliveryDate, PaymentStatus paymentStatus) {
        this.appUser = appUser;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.total = total;
        this.deliveryDate = deliveryDate;
        this.paymentStatus = paymentStatus;
    }

    public Order(AppUser appUser, OrderStatus orderStatus, DeliveryStatus deliveryStatus, BigDecimal total, LocalDateTime deliveryDate,
                 PaymentStatus paymentStatus, List<OrderLineItem> orderLineItem) {
        this.appUser = appUser;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.total = total;
        this.deliveryDate = deliveryDate;
        this.paymentStatus = paymentStatus;
        this.orderLineItem = orderLineItem;
    }
}
