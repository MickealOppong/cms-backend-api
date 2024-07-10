package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransCurrency extends LogEntity {

    @Id @GeneratedValue
    private Long recId;
    @NotBlank(message = "Please enter currency name")
    private String currency;
    @NotBlank(message = "Please provide currency ISO")
    private String iso;


    @JsonManagedReference
    @OneToMany
    private List<CurrencyRate> currencyRates = new ArrayList<>();



    public TransCurrency(String currency, String iso) {
        this.currency = currency;
        this.iso = iso;
    }
}
