package br.blog.smarti.ms.comunication.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class ApplicationBank {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationBank.class, args);
  }
}
