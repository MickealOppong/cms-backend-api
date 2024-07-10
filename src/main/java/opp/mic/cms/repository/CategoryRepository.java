package opp.mic.cms.repository;

import opp.mic.cms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
    Category save(Category category);
    Optional<Category> findById(Long id);


}
