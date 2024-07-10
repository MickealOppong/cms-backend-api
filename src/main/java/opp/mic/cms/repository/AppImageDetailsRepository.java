package opp.mic.cms.repository;

import opp.mic.cms.model.AppImageDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppImageDetailsRepository extends CrudRepository<AppImageDetails,Long> {
    Optional<AppImageDetails> findByPath(String path);
    List<AppImageDetails> findAll();

    @Query(value = "SELECT * FROM photos u where u.fk_uid=?",nativeQuery = true)
    Optional<AppImageDetails> findByUserId(Long id);

    @Query(value = "SELECT * FROM photos u where u.fk_pid=?",nativeQuery = true)
    List<AppImageDetails> findByProductId(Long id);
}
