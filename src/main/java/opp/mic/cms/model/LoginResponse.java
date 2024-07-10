package opp.mic.cms.model;

import lombok.Builder;

@Builder
public record LoginResponse (AppUser appUser,String accessToken,String token){
}
