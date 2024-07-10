package opp.mic.cms.controller;

import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.exceptions.UserNotFoundException;
import opp.mic.cms.model.AppUser;
import opp.mic.cms.model.RoleRequest;
import opp.mic.cms.model.UserInfoUpdateRequest;
import opp.mic.cms.model.UserListDTO;
import opp.mic.cms.service.AddressBokService;
import opp.mic.cms.service.UserRoleService;
import opp.mic.cms.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    private UserRoleService userRoleService;
    private PasswordEncoder passwordEncoder;
    private final AddressBokService addressBokService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          UserRoleService userRoleService ,AddressBokService addressBokService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
        this.addressBokService = addressBokService;
    }

    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN')")
    @GetMapping("/users")
    public Page<AppUser> all(int page) throws DataFormatException, IOException {
        return userService.getAll(page);
    }

    @PostMapping("/user")
    public ResponseEntity<String> add(@RequestParam String username, @RequestParam String password ,
                                      @RequestParam String fullname,@RequestParam String role,
                                      @RequestParam String gender,@RequestParam String telephone,
                                      MultipartFile file){

        AppUser appUser = new AppUser(username, fullname,
               telephone, passwordEncoder.encode(password),gender);
        appUser.setRoles(Set.of(userRoleService.getRole(role)));
        try {
         AppUser user= userService.save(appUser,file);
            return ResponseEntity.ok().body("User created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public AppUser appUser(@RequestParam Long id){
        try {
            return userService.getUser(id);
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN','SCOPE_ROLE_USER')")
    @GetMapping("/userList")
    public ResponseEntity<List<UserListDTO>> allUsers(){
        return ResponseEntity.ok().body(userService.all());
    }


    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN')")
    @PatchMapping("/role")
    public ResponseEntity<String> addRole(@RequestBody RoleRequest roleRequest) throws DataFormatException, IOException {
       AppUser appUser= userService.addRole(roleRequest.username(),roleRequest.rolename());
       if(appUser == null){
           return ResponseEntity.ok().body(roleRequest.rolename()+" already assigned to user");
       }
        return ResponseEntity.ok().body(roleRequest.rolename() +" added");
    }


    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        boolean appUser = userService.deleteUser(id);
        if(appUser){
            return ResponseEntity.ok().body("User deleted");
        }
        return ResponseEntity.badRequest().body("Cannot delete user,user has reference to role");
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<AppUser> updateUserData(@RequestParam Long id, @RequestParam(required = false) String username,
                                                 @RequestParam(required = false) String fullname,@RequestParam(required = false) String  street,@RequestParam String  city,
                                                 @RequestParam(required = false) String  zipCode,@RequestParam(required = false) String  country,
                                                 @RequestParam(required = false) String gender,@RequestParam(required = false) String telephone,
                                                 @RequestParam(required = false) boolean accountNonExpired, @RequestParam(required = false) boolean enabled,
                                                 @RequestParam(required = false) boolean accountNonLocked,@RequestParam(required = false) boolean credentialsNonExpired,
                                                 @RequestParam(required = false) MultipartFile image){

      //  appUser.setRoles(Set.of(userRoleService.getRole(role)))
            UserInfoUpdateRequest  infoUpdateRequest
                    = new UserInfoUpdateRequest(username,fullname
                    ,street,city,zipCode,country,accountNonExpired,enabled,accountNonLocked,credentialsNonExpired
                    ,telephone,gender);
            AppUser appUser = userService.updateUserInfo(id,infoUpdateRequest,image);
            if(appUser!= null){
                return ResponseEntity.ok().body(appUser);
            }
        return ResponseEntity.ok().body(null);

    }

}
