package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRate extends LogEntity {

    @Id @GeneratedValue
    private Long recId;
    private LocalDate date;
    private Double fxRate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "fk_currency",referencedColumnName = "recId")
    private TransCurrency transCurrency;

    public CurrencyRate(LocalDate date, Double fxRate) {
        this.date = date;
        this.fxRate = fxRate;
    }

    public CurrencyRate(LocalDate date, Double fxRate, TransCurrency transCurrency) {
        this.date = date;
        this.fxRate = fxRate;
        this.transCurrency = transCurrency;
    }
}
