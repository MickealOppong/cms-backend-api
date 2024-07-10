package opp.mic.cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import opp.mic.cms.util.LogEntity;

@Entity
@Table(name = "photos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppImageDetails extends LogEntity {

    @Id @GeneratedValue
    private Long id;
    private String type;
    private String path;
     @JsonBackReference
    @OneToOne
    @JoinColumn(name = "fk_uid",referencedColumnName = "id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "fk_pid",referencedColumnName = "id")
    private Product product;

    public AppImageDetails(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public AppImageDetails(String type, String path, AppUser appUser) {
        this.type = type;
        this.path = path;
        this.appUser = appUser;
    }
}
