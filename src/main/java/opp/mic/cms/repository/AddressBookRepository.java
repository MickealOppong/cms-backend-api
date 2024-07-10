package opp.mic.cms.repository;

import opp.mic.cms.model.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook,Long> {
    Optional<AddressBook> findByZipCode(String zipCode);

    @Query(value = "SELECT * FROM address_book u where u.fk_user=?",nativeQuery = true)
    Optional<AddressBook> findByUser(Long id);
}
