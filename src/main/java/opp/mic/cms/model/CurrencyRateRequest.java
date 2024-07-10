package opp.mic.cms.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CurrencyRateRequest {

    private Long id;
    private LocalDate from;
    private LocalDate to;

    public CurrencyRateRequest(Long id,String from,String to){
        this.id = id;
        this.from = LocalDate.parse(from);
       this.to = LocalDate.parse(to);
    }
}
