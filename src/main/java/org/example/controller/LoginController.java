package org.example.controller;

import org.example.dto.credentials.CredentialsDTO;
import org.example.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/login", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class LoginController {

    private final AuthenticationService authenticationService;

    @Autowired
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<Boolean> authenticateUser(@RequestBody CredentialsDTO credentialsDTO) {
        log.info("Endpoint '/api/login' was called to authenticate user");
        boolean successfulAuthentication = authenticationService.authenticateUser(
                credentialsDTO.getUsername(),
                credentialsDTO.getPassword()
        );

        return (successfulAuthentication)
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}
