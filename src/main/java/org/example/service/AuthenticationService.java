package org.example.service;

import org.example.utils.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

    private final UserAuthentication userAuthentication;

    @Autowired
    public AuthenticationService(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }

    public boolean authenticateUser(String username, String password) {
        boolean authenticationResult = userAuthentication.authenticateUser(username, password);
        if (authenticationResult) {
            log.info("User successfully authenticated");
        } else {
            log.warn("Authentication failed");
        }
        return authenticationResult;
    }
}
