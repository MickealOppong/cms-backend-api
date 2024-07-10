package opp.mic.cms.repository;

import opp.mic.cms.model.UserAuthority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAuthorityRepository extends CrudRepository<UserAuthority,Long> {
        @Query(value = "SELECT * from user_authority u where u.role_id=?" ,nativeQuery = true)
        List<UserAuthority> findByRoleId(Long roleId);
}
