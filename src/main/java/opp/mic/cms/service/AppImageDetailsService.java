package opp.mic.cms.service;

import opp.mic.cms.controller.AppImageController;
import opp.mic.cms.exceptions.InventoryNotFoundException;
import opp.mic.cms.impl.PhotoStorageServiceImpl;
import opp.mic.cms.model.AppImageDetails;
import opp.mic.cms.model.AppUser;
import opp.mic.cms.model.Product;
import opp.mic.cms.repository.AppImageDetailsRepository;
import opp.mic.cms.repository.ProductRepository;
import opp.mic.cms.repository.UserRepository;
import opp.mic.cms.util.PhotoStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AppImageDetailsService {


    private UserRepository userRepository;
    private AppImageDetailsRepository appImageDetailsRepository;
    private PhotoStorageServiceImpl photoStorageService;
    private PhotoStorageLocation storageLocation;
    private ProductRepository productRepository;


    public AppImageDetailsService(UserRepository userRepository, AppImageDetailsRepository appImageDetailsRepository,
                                  PhotoStorageServiceImpl photoStorageService,
                                  PhotoStorageLocation storageLocation, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.appImageDetailsRepository = appImageDetailsRepository;
        this.photoStorageService = photoStorageService;
        this.storageLocation = storageLocation;
        this.productRepository = productRepository;
    }

    @Transactional
    public AppUser uploadPhoto(MultipartFile file) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
       Optional<AppUser> appuUser = userRepository.findByUsername(username);
        if(appuUser.isPresent()) {
            AppUser user = appuUser.get();
            AppImageDetails appImageDetails = AppImageDetails.builder()
                    .path(storageLocation.getLocation() + "/" + user.getUsername() + "-" + file.getOriginalFilename())
                    .type(file.getContentType())
                    .build();
            appImageDetails.setAppUser(user);
            appImageDetailsRepository.save(appImageDetails);
            photoStorageService.store(file, user.getUsername());
            return user;
        }
        return null;
    }

    @Transactional
    public Product saveProductImage(Long id, MultipartFile file) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new InventoryNotFoundException("Could not find product with "+id));
            AppImageDetails appImageDetails = AppImageDetails.builder()
                    .path(storageLocation.getLocation() + "/" + product.getName() + "-" + file.getOriginalFilename())
                    .type(file.getContentType())
                    .build();
            appImageDetails.setProduct(product);
            appImageDetailsRepository.save(appImageDetails);
            photoStorageService.store(file, product.getName());
        return product;
    }

    @Transactional
    public Product saveProductImages(Long id, MultipartFile[] files) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(()->new InventoryNotFoundException("Could not find product with "+id));
        Arrays.stream(files).forEach(file->{
            AppImageDetails appImageDetails = AppImageDetails.builder()
            .path(storageLocation.getLocation() + "/" + product.getName() + "-" + file.getOriginalFilename())
                    .type(file.getContentType())
                    .build();
            appImageDetails.setProduct(product);
            appImageDetailsRepository.save(appImageDetails);
            photoStorageService.store(file, product.getName());
        });
        return product;
    }
    public Resource getResource(String path){
        return  photoStorageService.loadAsResource(path);
    }

    public List<String> getAll(Long id){
      List<AppImageDetails> imageDetails=  appImageDetailsRepository.findByProductId(id);
        List<String> urls = new ArrayList<>();
      for(AppImageDetails image: imageDetails){
          Resource uri = photoStorageService.loadAsResource(image.getPath());
        urls.add(MvcUriComponentsBuilder.fromMethodName(AppImageController.class, "serveFile",
                  uri.getFilename()).build().toUri().toString());
      }
       return urls;
    }
}
