package opp.mic.cms.service;

import opp.mic.cms.model.ContactUs;
import opp.mic.cms.repository.ContactUsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {


    private ContactUsRepository contactUsRepository;

    public ContactUsService(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }

    public Page<ContactUs> getAll(int page){
        PageRequest pageRequest = PageRequest.of(page,10);
        return contactUsRepository.findAll(pageRequest);
    }
    public void delete(Long id){
        contactUsRepository.deleteById(id);
    }

    public ContactUs getById(Long id){
        return contactUsRepository.findById(id).orElse(null);
    }
    public ContactUs getById(String email){
        return contactUsRepository.findByEmail(email).orElse(null);
    }
}
