package opp.mic.cms.repository;

import opp.mic.cms.model.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs,Long> {
    Optional<ContactUs> findByEmail(String email);
}
