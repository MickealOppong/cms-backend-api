package opp.mic.cms.service;

import opp.mic.cms.model.*;
import opp.mic.cms.repository.RoleRepository;
import opp.mic.cms.repository.UserAuthorityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleService {


    private RoleRepository roleRepository;
    private UserAuthorityRepository authorityRepository;
    private UserService userService;

    public UserRoleService(RoleRepository roleRepository,UserAuthorityRepository authorityRepository,UserService userService) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }

    @Transactional
    public void createUserAuthority(String model, String privilege, Roles roles){
        boolean exist = isAlreadyExist(roles.getRoleName());
        Roles role =null;
        if(!exist){
            role=  roleRepository.save(roles);
        }
        role= roleRepository.findByRoleName(roles.getRoleName()).get();
        UserAuthority authority = new UserAuthority(model,privilege,role);
        authorityRepository.save(authority);
    }

    private boolean isAlreadyExist(String roleName){
        Optional<Roles> role =roleRepository.findByRoleName(roleName);
        return role.isPresent();
    }

    public Page<Roles> allRoles(int page){
        PageRequest pageRequest = PageRequest.of(page,5);
        return roleRepository.findAll(pageRequest);
    }

    public RoleDTO getRole(Long id){
       Roles role =roleRepository.findById(id).orElse(null);
        Map<String,List<UserAuthority>> authMap = new HashMap<>();
        List<UserAuthority> authorityList= authorityRepository.findByRoleId(role.getId());

            for(UserAuthority authority: authorityList){
                authMap.put(authority.getModel(), authorityList.stream().filter(f->f.getModel().equals(authority.getModel()))
                        .map(p->new UserAuthority(p.getId(),p.getPrivilege(),p.getModel(),role)).collect(Collectors.toList()));
            }

        return new RoleDTO(role,authMap);
    }

    public Roles getRole(String role){
        return roleRepository.findByRoleName(role).orElse(null);
    }

    public List<RoleListDTO> all(){
       return roleRepository.findAll().stream().map(p->new RoleListDTO(p.getRoleName(), p.getId())).toList();
    }

    @Transactional
    public boolean deleteRole(Long id){
        long count = roleRepository.count();
        /*
        if(count ==1){
            return false;
        }

         */
        Roles role = roleRepository.findById(id).orElse(null);
        userService.editUserRole(role);
        roleRepository.save(role);
        List<UserAuthority> authorities= authorityRepository.findByRoleId(role.getId());
       authorities.forEach(i-> authorityRepository.delete(i));
       roleRepository.deleteById(id);
        return true;
    }
}
