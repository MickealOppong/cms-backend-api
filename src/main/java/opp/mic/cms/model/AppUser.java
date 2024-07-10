package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser extends LogEntity implements UserDetails, Serializable {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String fullname;
    private String Telephone;
    @JsonIgnore
    private String password;
    private String gender;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked ;
    boolean isCredentialsNonExpired ;
    boolean isEnabled ;

    @JsonManagedReference
    @OneToOne(mappedBy = "appUser")
    private AddressBook addressBook ;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "fk_role", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "fk_user", referencedColumnName = "id"))
    private Set<Roles> roles  = new HashSet<>();

    @Transient
    private String image;


    public AppUser(String username, String fullname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }

    public AppUser(String username, String fullname, String password,Roles role) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        roles.add(role);
    }

    public AppUser(String username, String fullname, String telephone, String password, String gender) {
        this.username = username;
        this.fullname = fullname;
        Telephone = telephone;
        this.password = password;
        this.gender = gender;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Roles role: roles){
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
            authorities.addAll(role.getAuthorities().stream()
                    .map(p->new SimpleGrantedAuthority(p.getPrivilege())).toList());
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
