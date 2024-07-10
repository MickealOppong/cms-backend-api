package opp.mic.cms.repository;

import opp.mic.cms.model.TransCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransCurrencyRepository extends JpaRepository<TransCurrency,Long> {
    Optional<TransCurrency> findByCurrency(String currency);
}
