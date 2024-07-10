package opp.mic.cms.repository;

import opp.mic.cms.model.VendorTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VendorTransactionRepository extends CrudRepository<VendorTransaction,Long> {
    List<VendorTransaction> findByVendorId(Long id);
}
