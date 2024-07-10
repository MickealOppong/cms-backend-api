package opp.mic.cms.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opp.mic.cms.impl.PhotoStorageServiceImpl;
import opp.mic.cms.model.*;
import opp.mic.cms.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Slf4j
@EnableMethodSecurity(securedEnabled = true)
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final RsaKeyProperties rsaKey;
    private UserRepository userRepository;
    private final PhotoStorageServiceImpl photoStorageServiceImpl;



    @Bean
    public CommandLineRunner init(){
        return args -> photoStorageServiceImpl.init();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User does not exist"));
    }


    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKey.publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKey.publicKey()).privateKey(rsaKey.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("https://e-cms.netlify.app"));
        config.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT","PATCH","OPTIONS","HEAD"));
        config.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers","Access-Control-Allow-Origin","Access-Control-Request-Method", "Access-Control-Request-Headers","Origin","Cache-Control", "Content-Type", "Authorization"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request->request
                .requestMatchers("/api/auth","/api/auth/**","/api/photos","/api/photos/**","/api/products","/api/products/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(rs->rs.jwt(Customizer.withDefaults()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("SCOPE");
    }

    @Bean
    public CommandLineRunner load(UserRepository userRepository, RoleRepository repository, CategoryRepository repo,
                                  AttributesRepository attRep, ProductSKURepository sku, UserAuthorityRepository authRepo,AddressBookRepository addRep){
        return args -> {

           Roles eppsRole = new Roles("USER");
            Roles demoRole = new Roles("ADMIN");

          repository.saveAll(List.of(eppsRole,demoRole));

            List<UserAuthority> authorities = new ArrayList<>();

            authorities.add(new UserAuthority("Products","view",eppsRole));
            authorities.add(new UserAuthority("Orders","create",eppsRole));

            authorities.add(new UserAuthority("Users","view",demoRole));
            authorities.add(new UserAuthority("Roles","create",demoRole));

            authorities.forEach(authRepo::save);
            /*
            AppUser epps = new AppUser("epps@mail.com","Mike Epps",passwordEncoder().encode("password")
           );
            epps.setEnabled(true);

            epps.setGender("Female");
            epps.setRoles(Set.of(eppsRole));

             */
            AppUser demo = new AppUser("demo@mail.com","demo user",passwordEncoder().encode("demo-user")
           );

          demo.setRoles(Set.of(demoRole,eppsRole));
            demo.setGender("Male");
            demo.setEnabled(true);
            demo.setCredentialsNonExpired(true);
            demo.setAccountNonExpired(true);
            demo.setAccountNonLocked(true);

           // userRepository.save(epps);
            userRepository.save(demo);

        };
    }
}
