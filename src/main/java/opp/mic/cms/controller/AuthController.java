package opp.mic.cms.controller;

import opp.mic.cms.model.AppUser;
import opp.mic.cms.model.AuthRequest;
import opp.mic.cms.model.LoginResponse;
import opp.mic.cms.model.RefreshToken;
import opp.mic.cms.service.RefreshTokenService;
import opp.mic.cms.service.UserAuthTokenService;
import opp.mic.cms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authManager;
    private UserService userService;
    private RefreshTokenService refreshTokenService;
    private UserAuthTokenService authTokenService;
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, UserService userService,
                          RefreshTokenService refreshTokenService, UserAuthTokenService authTokenService,PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authTokenService = authTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String hello(){
        return "hello";
    }
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> token(@RequestBody AuthRequest authRequest){
        try{
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(),authRequest.password()));
            if(authentication.isAuthenticated()){
                RefreshToken refreshToken = refreshTokenService.createToken(authentication.getName());
                LoginResponse loginResponse = LoginResponse.builder()
                        .appUser(userService.getUser(authentication.getName()))
                        .accessToken(authTokenService.token(authentication))
                        .token(refreshToken.getToken())
                        .build();
                return ResponseEntity.ok().body(loginResponse);
            }
            else{
                throw new AuthenticationServiceException("Could not authenticate user");
            }
        }catch(Exception e){
            throw  new AuthenticationServiceException(e.getMessage());
        }
    }

    @DeleteMapping("/logout")
    public RefreshToken logout(@RequestParam("token") String token){
        return refreshTokenService.removeToken(token);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestParam String username, @RequestParam String password, @RequestParam String fullname){
        AppUser user = new AppUser(username,fullname,passwordEncoder.encode(password));
        return ResponseEntity.ok(userService.register(user));
    }
}
