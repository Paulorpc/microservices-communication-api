package br.blog.smarti.ms.communication.bank.securities;

import static br.blog.smarti.ms.communication.bank.securities.SecurityRolesEnum.*;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtils {

  public static boolean isUser(Authentication authentication) {
    return isUserInRole(USER.getValue(), authentication);
  }

  public static boolean isAdmin(Authentication authentication) {
    return isUserInRole(ADMIN.getValue(), authentication);
  }

  public static boolean isUserInRole(final String role, Authentication authentication) {
    @SuppressWarnings("unchecked")
    Collection<GrantedAuthority> authorities =
        (Collection<GrantedAuthority>) authentication.getAuthorities();

    final String roleWithPrefix = PREFIX.getValue() + role;

    return authorities.stream()
        .anyMatch(authority -> roleWithPrefix.equals(authority.getAuthority()));
  }
}
