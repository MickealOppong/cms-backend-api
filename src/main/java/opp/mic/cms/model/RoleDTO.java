package opp.mic.cms.model;

import java.util.List;
import java.util.Map;

public record RoleDTO(Roles role, Map<String, List<UserAuthority>> authorities) {
}
