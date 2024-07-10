package opp.mic.cms.service;

import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.controller.AppImageController;
import opp.mic.cms.exceptions.UserNotFoundException;
import opp.mic.cms.exceptions.UserRoleNotFound;
import opp.mic.cms.impl.PhotoStorageServiceImpl;
import opp.mic.cms.model.*;
import opp.mic.cms.repository.*;
import opp.mic.cms.util.PhotoStorageLocation;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AppImageDetailsRepository appImageDetailsRepository;
    private final PhotoStorageServiceImpl photoStorageService;
    private final PhotoStorageLocation storageLocation;
    private final RoleRepository roleRepository;
    private final UserAuthorityRepository authorityRepository;
    private final AddressBookRepository addressBookRepository;


    public UserService(UserRepository userRepository, AppImageDetailsRepository appImageDetailsRepository,
                       PhotoStorageServiceImpl photoStorageService, PhotoStorageLocation storageLocation,
                       RoleRepository roleRepository, UserAuthorityRepository authorityRepository,AddressBookRepository addressBookRepository) {
        this.userRepository = userRepository;
        this.appImageDetailsRepository = appImageDetailsRepository;
        this.photoStorageService = photoStorageService;
        this.storageLocation = storageLocation;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.addressBookRepository = addressBookRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // UserDetails userDetails = new AppUser();
       AppUser appUser =userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User does not exist"));
        return new User(appUser.getUsername(), appUser.getPassword(),appUser.getAuthorities());
    }

    public AppUser getUser(String username){
        Optional<AppUser> user =  userRepository.findByUsername(username);
        if (user.isPresent()) {
            Optional<AppImageDetails> photo = appImageDetailsRepository.findByUserId(user.get().getId());
            if (photo.isPresent()) {
                Resource uri = photoStorageService.loadAsResource(photo.get().getPath());
                String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class, "serveFile",
                        uri.getFilename()).build().toUri().toString();
                user.get().setImage(url);
            }
        }
        return user.orElse(null);
    }

    public Page<AppUser> getAll(int page) throws DataFormatException, IOException {
        PageRequest pr = PageRequest.of(page,5);
       Page<AppUser> userList=  userRepository.findAll(pr);
      for(AppUser appUser :userList){
         Optional<AppImageDetails> photo= appImageDetailsRepository.findByUserId(appUser.getId());
         if (photo.isPresent()){
            Resource uri =photoStorageService.loadAsResource(photo.get().getPath());
             String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class,"serveFile",
                     uri.getFilename()).build().toUri().toString();
             appUser.setImage(url);
         }
      }
      return userList;
    }

    public List<AppUser> getAll() throws DataFormatException, IOException {
        List<AppUser> userList=  userRepository.findAll();
        for(AppUser appUser :userList){
            Optional<AppImageDetails> photo= appImageDetailsRepository.findByUserId(appUser.getId());
            if (photo.isPresent()){
                Resource uri =photoStorageService.loadAsResource(photo.get().getPath());
                String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class,"serveFile",
                        uri.getFilename().toString()).build().toUri().toString();
                appUser.setImage(url);

            }
        }
        System.out.println(userList);
        return userList;
    }

    public AppUser  getUser(Long id) throws DataFormatException, IOException {
        Optional<AppUser> user =  userRepository.findById(id);
        if (user.isPresent()) {
            Optional<AppImageDetails> photo = appImageDetailsRepository.findByUserId(user.get().getId());
            if (photo.isPresent()) {
                Resource uri = photoStorageService.loadAsResource(photo.get().getPath());
                String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class, "serveFile",
                        uri.getFilename()).build().toUri().toString();
                user.get().setImage(url);
            }
        }
        return user.orElse(null);
    }

    @Transactional
    public AppUser save(AppUser appUser, MultipartFile file) throws IOException {
        AppUser user = userRepository.save(appUser);
        AppImageDetails appImageDetails = AppImageDetails.builder()
                .path(storageLocation.getLocation()+"/"+user.getUsername()+"-"+file.getOriginalFilename())
                .type(file.getContentType())
                .build();
        appImageDetails.setAppUser(user);
        appImageDetailsRepository.save(appImageDetails);
        photoStorageService.store(file,user.getUsername());
        return user;
    }

    public AppUser save(AppUser appUser) throws IOException {
      return userRepository.save(appUser);
    }

    public List<UserListDTO> all(){
      List<AppUser> users= userRepository.findAll();
      List<UserListDTO> userList = new ArrayList<>();
      for(AppUser user :users){
        AppImageDetails photo=  appImageDetailsRepository.findByUserId(user.getId()).orElse(null);
        if(photo != null){
            Resource uri = photoStorageService.loadAsResource(photo.getPath());
            String url = MvcUriComponentsBuilder.fromMethodName(AppImageController.class, "serveFile",
                    uri.getFilename()).build().toUri().toString();
            user.setImage(url);
        }
        userList.add(new UserListDTO(user.getFullname(),user.getId(),user.getImage()));
      }
      return userList;
    }

    public AppUser addRole(String username,String roleName)  {
      AppUser appUser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(""));
        appUser.getRoles().add(roleRepository.findByRoleName(roleName).orElseThrow(()->new UserRoleNotFound("")));
        userRepository.save(appUser);
      return appUser;
    }


    public boolean deleteUser(Long id){
       long users= userRepository.count();
       /*
       if(users==1){
           return false;
       }
        */
        AppUser appUser = userRepository.findById(id).orElse(null);
       if(!appUser.getRoles().isEmpty()){
           return false;
       }
        userRepository.deleteById(id);
      return true;

    }

    public void editUserRole(Long id, Roles role){
       AppUser appUser= userRepository.findById(id).orElse(null);
       appUser.getRoles().remove(role);
       userRepository.save(appUser);
    }
    public void editUserRole(Roles role){
        List<AppUser> appUser= userRepository.findAll();
        appUser.forEach(user-> user.getRoles().remove(role));
        userRepository.saveAll(appUser);
    }

    @Transactional
    public AppUser updateUserInfo(Long id,UserInfoUpdateRequest infoUpdateRequest,MultipartFile file){
        AppUser appUser = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("user does not exist"));
        if(infoUpdateRequest.fullname()!=null){
            appUser.setFullname(infoUpdateRequest.fullname());
        }
        if(infoUpdateRequest.username()!=null){
            appUser.setUsername(infoUpdateRequest.username());
        }
        if(infoUpdateRequest.telephone()!=null){
            appUser.setTelephone(infoUpdateRequest.telephone());
        }
        if(infoUpdateRequest.gender()!=null){
            appUser.setGender(infoUpdateRequest.gender());
        }
            appUser.setAccountNonExpired(infoUpdateRequest.accountNonExpired());

            appUser.setAccountNonLocked(infoUpdateRequest.accountNonLocked());

            appUser.setEnabled(infoUpdateRequest.enabled());

            appUser.setCredentialsNonExpired(infoUpdateRequest.credentialsNonExpired());

        AddressBook addressBook =addressBookRepository.findByUser(appUser.getId()).orElse(null);
        if(addressBook !=null){
            if(infoUpdateRequest.street()!=null){
                addressBook.setStreet(infoUpdateRequest.street());
            }
            if(infoUpdateRequest.city()!=null){
                addressBook.setCity(infoUpdateRequest.city());
            }
            if(infoUpdateRequest.zipCode()!=null){
                addressBook.setZipCode(infoUpdateRequest.zipCode());
            }
            if(infoUpdateRequest.country()!=null){
                addressBook.setCountry(infoUpdateRequest.country());
            }
            addressBookRepository.save(addressBook);
        }else{
            AddressBook newAddress = new AddressBook(infoUpdateRequest.street(), infoUpdateRequest.city(), infoUpdateRequest.zipCode(),
                    infoUpdateRequest.country(),appUser);
            addressBookRepository.save(newAddress);
        }
        if(!file.isEmpty()){
            photoStorageService.store(file, infoUpdateRequest.username());
            Optional<AppImageDetails> imageDetails = appImageDetailsRepository.findByUserId(id);
            if (imageDetails.isPresent()) {
                AppImageDetails image = imageDetails.get();
                image.setPath(storageLocation.getLocation() + "/" + appUser.getUsername() + "-" + file.getOriginalFilename());
                image.setType(file.getContentType());
                appImageDetailsRepository.save(imageDetails.get());
            } else {
                AppImageDetails appImageDetails = new AppImageDetails(file.getContentType(), storageLocation.getLocation() + "/" + appUser.getUsername() + "-" + file.getOriginalFilename(), appUser);
                appImageDetailsRepository.save(appImageDetails);
            }
        }

       return   userRepository.save(appUser);

    }

    public AppUser register(AppUser appUser)  {
        appUser.setCredentialsNonExpired(true);
        appUser.setAccountNonLocked(true);
        appUser.setAccountNonExpired(true);
        appUser.setEnabled(true);
        Optional<Roles> userRole = roleRepository.findByRoleName("USER");
        userRole.ifPresent(roles -> appUser.setRoles(Set.of(roles)));
        return userRepository.save(appUser);
    }
}