package br.blog.smarti.ms.communication.buyprocess.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeycloackTokenRequestDto {

  private String grant_type;
  private String username;
  private String password;
  private String client_secret;
  private String client_id;

  public MultiValueMap<String, String> asMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", client_id);
    map.add("client_secret", client_secret);
    map.add("grant_type", grant_type);
    map.add("username", username);
    map.add("password", password);
    return map;
  }
}
