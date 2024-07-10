package opp.mic.cms.repository;

import opp.mic.cms.model.Vendor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends CrudRepository<Vendor,Long> {
    Optional<Vendor> findByName(String name);
    List<Vendor> findAll();
}
