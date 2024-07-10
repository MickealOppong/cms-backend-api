package opp.mic.cms.repository;

import opp.mic.cms.model.UserAuthority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRightsRepository extends CrudRepository<UserAuthority,Long> {

}
