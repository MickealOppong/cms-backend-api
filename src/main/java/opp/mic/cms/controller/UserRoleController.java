package opp.mic.cms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import opp.mic.cms.model.*;
import opp.mic.cms.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/roles")
public class UserRoleController {

    private UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PreAuthorize("hasAuthority('SCOPE_Roles:create')")
    @PostMapping("/roles")
    public ResponseEntity<String> roles(@RequestBody RolesRequest rolesRequest) throws JsonProcessingException {
        //SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(System.out::println);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = rolesRequest.getModel();
        UserAccess[] accesses = objectMapper.readValue(json, UserAccess[].class);
        Arrays.stream(accesses).forEach(i->userRoleService.createUserAuthority(i.getModel(),i.getText(),new Roles(rolesRequest.getRoleName())));
        //System.out.println(Arrays.toString(accesses));
        return ResponseEntity.ok().body("Nothing done");
    }
    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN','SCOPE_ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<Page<Roles>> allRoles(int page){
        return ResponseEntity.ok().body(userRoleService.allRoles(page));
    }

    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN','SCOPE_ROLE_USER')")
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> role(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(userRoleService.getRole(id));
    }

    @PreAuthorize("hasAnyRole('SCOPE_ROLE_MANAGER','SCOPE_ROLE_ADMIN','SCOPE_ROLE_USER')")
    @GetMapping("/roleList")
    public ResponseEntity<List<RoleListDTO>> roleList(){
        return ResponseEntity.ok().body(userRoleService.all());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        boolean role = userRoleService.deleteRole(id);
        if(role){
            return ResponseEntity.ok().body("Role deleted");
        }
        return ResponseEntity.badRequest().body("Cannot delete role,there must be at least one role in the repository");
    }
}
