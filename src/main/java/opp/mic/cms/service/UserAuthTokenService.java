package opp.mic.cms.service;

import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.model.AppUser;
import opp.mic.cms.repository.RefreshTokenRepository;
import opp.mic.cms.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAuthTokenService {

    private JwtEncoder jwtEncoder;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    public UserAuthTokenService(JwtEncoder jwtEncoder,
                                RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String token(Authentication authentication){

      // String scope = userRepository.findByUsername(authentication.getName())
        //       .map(AppUser::getAuthorities).get().toString();
        Set<String> roles = AuthorityUtils.authorityListToSet(userRepository.findByUsername(authentication.getName())
                        .map(AppUser::getAuthorities).get())
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("local")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(5, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope",roles)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
