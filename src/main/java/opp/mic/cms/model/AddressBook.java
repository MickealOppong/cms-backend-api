package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook extends LogEntity {
    @Id @GeneratedValue
    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String country;

    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user",referencedColumnName = "id")
    private AppUser appUser;


    public AddressBook(String street, String city, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public AddressBook(String street, String city, String zipCode, String country, AppUser appUser) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.appUser = appUser;
    }


}
