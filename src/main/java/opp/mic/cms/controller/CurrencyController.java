package opp.mic.cms.controller;

import jakarta.validation.Valid;
import opp.mic.cms.model.CurrencyRate;
import opp.mic.cms.model.CurrencyRateRequest;
import opp.mic.cms.model.TransCurrency;
import opp.mic.cms.service.TransCurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final TransCurrencyService transCurrencyService;

    public CurrencyController(TransCurrencyService transCurrencyService) {
        this.transCurrencyService = transCurrencyService;
    }


    @GetMapping("/all")
    public Page<TransCurrency> all(int page){
       return transCurrencyService.all(page);

    }

    @GetMapping("/rates")
    public List<CurrencyRate> all(@RequestParam Long id,@RequestParam(required = false)
    String from,@RequestParam(required = false) String to){
        CurrencyRateRequest currencyRateRequest = new CurrencyRateRequest(id,from,to);
        return transCurrencyService.allRates(currencyRateRequest.getId(),currencyRateRequest.getFrom(),currencyRateRequest.getTo());
    }


    @GetMapping("/rate/{iso}")
    public CurrencyRate getRate(@RequestParam String iso){
       return transCurrencyService.getByCurrency(iso);
    }

    @PostMapping("/fx/{id}")
    public String addRate(@RequestParam Long id, @RequestParam String date, @RequestParam Double rate){
      CurrencyRate fxRate =transCurrencyService.addRate(id,LocalDate.parse(date),rate);
        if(fxRate != null){
            return "Currency created";
        }
        return "Unable to perform operation";
    }


    @DeleteMapping("/{recId}")
    public String deleteRate(@RequestParam Long recId){
        Optional<CurrencyRate> fxRate =transCurrencyService.removeRate(recId);
        if(fxRate.isEmpty()){
            return "Rate deleted";
        }
        return "Unable to perform operation";
    }

    @DeleteMapping("/deleteCurrency")
    public void deleteCurrency(@RequestParam Long recId){
       transCurrencyService.removeCurrency(recId);
    }

    @PostMapping("/currency")
    public String addCurrency(@Valid @RequestBody TransCurrency transCurrency){
      TransCurrency currency=  transCurrencyService.addCurrency(transCurrency);
      if(currency != null){
          return "Currency created";
      }
      return "Unable to perform operation";
    }
}
