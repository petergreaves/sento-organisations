package com.sento.organisations;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
@RestController
public class OrganisationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganisationServiceApplication.class, args);
    }


}
