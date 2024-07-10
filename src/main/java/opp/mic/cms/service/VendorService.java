package opp.mic.cms.service;


import opp.mic.cms.model.Vendor;
import opp.mic.cms.model.VendorTransaction;
import opp.mic.cms.repository.VendorRepository;
import opp.mic.cms.repository.VendorTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private VendorRepository vendorRepository;
    private VendorTransactionRepository vendorTransactionRepository;

    public VendorService(VendorRepository vendorRepository, VendorTransactionRepository vendorTransactionRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorTransactionRepository =vendorTransactionRepository;
    }


    public Vendor save(Vendor vendor){
        return vendorRepository.save(vendor);
    }

    public Optional<Vendor> getVendorByName(String name){
        return vendorRepository
                .findByName(name);
    }
    public List<String> allVendors(){
        return vendorRepository.findAll().stream().map(Vendor::getName).toList();
    }

    public VendorTransaction saveTransaction(VendorTransaction vendorTransaction){
        return vendorTransactionRepository.save(vendorTransaction);
    }
}
