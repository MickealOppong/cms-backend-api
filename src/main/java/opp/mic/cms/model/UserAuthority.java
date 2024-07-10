package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority implements Serializable {

    @Id @GeneratedValue
    private Long id;
    private String privilege;
    private String model;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId",referencedColumnName = "id")
    private Roles roles;

    public UserAuthority( String model, String privilege,Roles roles) {
        this.privilege =model+":"+privilege;
        this.model = model;
        this.roles = roles;
    }
}
