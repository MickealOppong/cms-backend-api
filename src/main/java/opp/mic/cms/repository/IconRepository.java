package opp.mic.cms.repository;

import opp.mic.cms.model.IconData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IconRepository extends CrudRepository<IconData,Long> {
    @Query(value = "SELECT * FROM icon_data u where u.fk_id=?",nativeQuery = true)
    Optional<IconData> findByFKId(Long id);
}
