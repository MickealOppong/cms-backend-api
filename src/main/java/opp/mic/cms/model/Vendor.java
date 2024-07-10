package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;


    public Vendor(String name, String address, String telephone, String email) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }
}
