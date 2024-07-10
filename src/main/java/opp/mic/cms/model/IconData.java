package opp.mic.cms.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IconData {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String type;

    @OneToOne
    @JoinColumn(name = "fk_id",referencedColumnName = "id")
    private Category category;

    public IconData(String name, String type,Category category) {
        this.name = name;
        this.type = type;
        this.category = category;
    }

}
