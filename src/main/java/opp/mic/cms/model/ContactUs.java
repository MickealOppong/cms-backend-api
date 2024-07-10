package opp.mic.cms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactUs {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String telephone;
    private String message;

    public ContactUs(String name, String email, String telephone, String message) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.message = message;
    }
}
