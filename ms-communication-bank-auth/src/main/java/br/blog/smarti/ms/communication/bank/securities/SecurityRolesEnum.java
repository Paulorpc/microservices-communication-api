package br.blog.smarti.ms.communication.bank.securities;

public enum SecurityRolesEnum {
  PREFIX("ROLE_"),
  ADMIN("admin"),
  USER("user");

  private String value;

  SecurityRolesEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
