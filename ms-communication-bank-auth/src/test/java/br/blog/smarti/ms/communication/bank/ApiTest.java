package br.blog.smarti.ms.communication.bank;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
public class ApiTest {

  @Autowired protected MockMvc mvc;

  @Autowired @Getter private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void init() {
    mvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            //.alwaysDo(print())
            .apply(springSecurity())
            .build();

    this.setup();
  }

  void setup() {}
}
