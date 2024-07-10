package opp.mic.cms.repository;

import opp.mic.cms.model.ProductVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariationsRepository extends CrudRepository<ProductVariation,Long> {

    List<ProductVariation> findByProductId(Long id);
}
