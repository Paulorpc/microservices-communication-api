package br.blog.smarti.ms.communication.buyprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan
public class ApplicationBuyProcess {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBuyProcess.class, args);
    }

}