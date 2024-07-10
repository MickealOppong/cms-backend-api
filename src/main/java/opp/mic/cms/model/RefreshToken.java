package opp.mic.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    private String token;
    private Instant expirationTime;

    @ManyToOne
    @JoinColumn(name = "fk_id",referencedColumnName = "id")
    private AppUser appUser;

    public RefreshToken(String token, Instant expirationTime, AppUser appUser) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.appUser = appUser;
    }
}
