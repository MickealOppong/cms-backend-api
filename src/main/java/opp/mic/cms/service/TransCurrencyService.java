package opp.mic.cms.service;

import opp.mic.cms.controller.CurrencyRateDTO;
import opp.mic.cms.model.CurrencyRate;
import opp.mic.cms.model.TransCurrency;
import opp.mic.cms.repository.CurrencyRateRepository;
import opp.mic.cms.repository.TransCurrencyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class TransCurrencyService {

    private final CurrencyRateRepository currencyRateRepository;
    private final TransCurrencyRepository transCurrencyRepository;


    public TransCurrencyService(CurrencyRateRepository currencyRateRepository,
                                TransCurrencyRepository transCurrencyRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.transCurrencyRepository = transCurrencyRepository;
    }

    public TransCurrency addCurrency(TransCurrency transCurrency){
      return  transCurrencyRepository.save(transCurrency);
    }

    public CurrencyRate addRate(Long id , LocalDate date,Double fxRate){
       Optional<TransCurrency> currency = transCurrencyRepository.findById(id);
       if(currency.isPresent()){
           TransCurrency transCurrency = currency.get();
           CurrencyRate rate = new CurrencyRate(date,fxRate,transCurrency);
          return currencyRateRepository.save(rate);
       }
       return null;
    }

    public Page<TransCurrency> all(int pageNumber){
        PageRequest pageRequest = PageRequest.of(pageNumber,5);
      return transCurrencyRepository.findAll(pageRequest);
    }

    public CurrencyRateDTO allRates(int pageNumber,Long id){
        PageRequest pageRequest = PageRequest.of(pageNumber,5);
       Page<CurrencyRate> rates=currencyRateRepository.findAll(pageRequest);
       List<CurrencyRate> selectedCurrency = rates.stream().filter(item->item.getTransCurrency().getRecId().equals(id)).toList();
        return new CurrencyRateDTO(selectedCurrency,rates.getTotalPages() ,rates.getSize(),rates.getNumber());
    }
    public List<CurrencyRate> allRates(Long id,LocalDate from,LocalDate to){
      List<CurrencyRate> currencyRates = currencyRateRepository.findAll().stream()
              .filter(item->item.getTransCurrency().getRecId().equals(id)).toList();
      return currencyRates.stream()
              .filter(f->(f.getDate().isAfter(from) || f.getDate().equals(from))
                      && (f.getDate().isBefore(to) || f.getDate().equals(to))).toList();
    }

    public Optional<TransCurrency> getCurrencyRate(String currency){
        return  transCurrencyRepository.findByCurrency(currency);
    }

    public CurrencyRate getByCurrency(String iso){
      List<CurrencyRate> rates = currencyRateRepository.findAll().stream()
              .filter(currency->currency.getTransCurrency().getIso().equals(iso)).toList();
      CurrencyRate currencyRate = null;
        Stack<CurrencyRate> currencyRateStack = new Stack<>();
      for(CurrencyRate rate : rates){
          if(rate.getDate().getMonth().equals(LocalDate.now().getMonth())){
              currencyRateStack.push(rate);
          }
      }
      currencyRateStack.sort(Comparator.comparing(CurrencyRate::getDate));
      return currencyRateStack.pop();
    }


    public Optional<CurrencyRate> removeRate(Long id){
        currencyRateRepository.deleteById(id);
        return currencyRateRepository.findById(id);
    }

    public void removeCurrency(Long id){
       transCurrencyRepository.deleteById(id);
    }
}
