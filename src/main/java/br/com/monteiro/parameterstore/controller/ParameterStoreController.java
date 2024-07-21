package br.com.monteiro.parameterstore.controller;

import br.com.monteiro.parameterstore.configuration.ParameterStoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ParameterStoreController {

    @Autowired
    private ParameterStoreConfiguration configuration;

    @GetMapping("/parameterStoreConfiguration")
    public String configuration() {
        return configuration.getHelloWorld();
    }

}
