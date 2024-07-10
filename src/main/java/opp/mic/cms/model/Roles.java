package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles extends LogEntity  {

    @Id @GeneratedValue
    private Long id;
    private String roleName;
    @JsonBackReference
    @OneToMany(mappedBy = "roles",fetch = FetchType.EAGER )
    private List<UserAuthority> authorities= new ArrayList<>();


    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AppUser> appUser = new HashSet<>();


    public Roles(String roleName) {
        this.roleName = roleName;
    }

    public void removeUsers(){
        this.getAppUser().remove(this);
    }

    public void removeAuthorities(List<UserAuthority> authorities){
        this.authorities.removeAll(authorities);
    }

}
