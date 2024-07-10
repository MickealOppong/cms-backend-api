package opp.mic.cms.service;

import opp.mic.cms.model.AppUser;
import opp.mic.cms.model.RefreshToken;
import opp.mic.cms.repository.RefreshTokenRepository;
import opp.mic.cms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .appUser(userRepository.findByUsername(username).get())
                .expirationTime(Instant.now().plus(1, ChronoUnit.HOURS))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken isTokenExpired(RefreshToken refreshToken){
        if(refreshToken.getExpirationTime().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken removeToken(String refreshToken){
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);
        token.ifPresent(value -> refreshTokenRepository.delete(value));
        return token.get();
    }

    public Optional<RefreshToken> getByUsername(String username){
        Optional<AppUser> appUser = userRepository.findByUsername(username);
        return appUser.flatMap(user -> refreshTokenRepository.findById(user.getId()));
    }
}
