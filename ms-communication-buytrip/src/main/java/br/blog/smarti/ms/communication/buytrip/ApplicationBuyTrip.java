package br.blog.smarti.ms.communication.buytrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class ApplicationBuyTrip {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationBuyTrip.class, args);
  }
}
