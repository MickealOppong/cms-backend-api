package opp.mic.cms.service;

import opp.mic.cms.exceptions.AddressNotFoundException;
import opp.mic.cms.model.AddressBook;
import opp.mic.cms.repository.AddressBookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBokService {


    private final AddressBookRepository addressBookRepository;

    public AddressBokService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }


    public List<AddressBook> all(){
        return addressBookRepository.findAll();
    }

    public Page<AddressBook> All(int page){
        PageRequest pageRequest = PageRequest.of(page,10);
        return addressBookRepository.findAll(pageRequest);
    }

    public void delete(Long id){
        addressBookRepository.deleteById(id);
    }

    public AddressBook getOne(Long id){
        return addressBookRepository.findById(id).orElse(null);
    }

    public AddressBook getOne(String zipCode){
        return addressBookRepository.findByZipCode(zipCode).orElse(null);
    }

    public AddressBook updateAddress(Long id,AddressBook addressBook){
        AddressBook savedAddress = addressBookRepository.findById(id)
                .orElseThrow(()-> new AddressNotFoundException("Could not find the address in the repository"));
        if(addressBook.getStreet() !=null){
            savedAddress.setStreet(addressBook.getStreet());
        }
        if(addressBook.getCity() !=null){
            savedAddress.setCity(addressBook.getCity());
        }
        if(addressBook.getZipCode() != null){
            savedAddress.setZipCode(addressBook.getZipCode());
        }
        if(addressBook.getCountry() != null){
            savedAddress.setCountry(addressBook.getCountry());
        }
       return addressBookRepository.save(savedAddress);
    }


}
