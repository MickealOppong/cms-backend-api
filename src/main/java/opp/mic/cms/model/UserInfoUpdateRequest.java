package opp.mic.cms.model;

public record UserInfoUpdateRequest(String username,String fullname,String street,String city,String zipCode,String country,
boolean accountNonExpired,boolean enabled,boolean accountNonLocked,boolean credentialsNonExpired,String telephone,String gender) {
}
