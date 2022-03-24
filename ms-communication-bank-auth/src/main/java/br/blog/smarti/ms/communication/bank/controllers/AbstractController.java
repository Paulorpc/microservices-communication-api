package br.blog.smarti.ms.communication.bank.controllers;

import static br.blog.smarti.ms.communication.bank.securities.SecurityRolesEnum.ADMIN;
import static br.blog.smarti.ms.communication.bank.securities.SecurityRolesEnum.USER;

import br.blog.smarti.ms.communication.bank.securities.SecurityUtils;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

/***
 *  Notes about keycloack security context
 *  https://lists.jboss.org/pipermail/keycloak-user/2017-October/011948.html
 */
public abstract class AbstractController {

  private static final String ROLE_ADMIN = ADMIN.getValue();
  private static final String ROLE_USER = USER.getValue();
  private static final String ATTR_EMAIL = "email";
  private static final String ATTR_USERNAME = "username";
  private static final String ATTR_ESTADOCIVIL = "estadocivil";

  @Autowired private KeycloakSecurityContext securityContext;

  public String getEmail() {
    return StringUtils.defaultIfBlank(
        (String) securityContext.getToken().getOtherClaims().get(ATTR_EMAIL),
        securityContext.getToken().getEmail());
  }

  public String getUsername() {
    return StringUtils.defaultIfBlank(
        (String) securityContext.getToken().getOtherClaims().get(ATTR_USERNAME),
        securityContext.getToken().getPreferredUsername());
  }

  public String getEstadoCivil() {
    return StringUtils.defaultIfBlank(
        (String) securityContext.getToken().getOtherClaims().get(ATTR_ESTADOCIVIL), "");
  }

  protected boolean isUserInRole(final String role) {
    return SecurityUtils.isUserInRole(role, SecurityContextHolder.getContext().getAuthentication());
  }

  protected boolean isAdmin() {
    return isUserInRole(ROLE_ADMIN);
  }

  protected boolean isUser() {
    return isUserInRole(ROLE_USER);
  }

  protected ResponseEntity<?> processOnlyIf(
      BooleanSupplier test, Supplier<ResponseEntity<?>> runnable) {
    if (!test.getAsBoolean()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    return runnable.get();
  }
}
