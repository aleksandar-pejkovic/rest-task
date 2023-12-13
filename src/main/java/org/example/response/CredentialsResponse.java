package org.example.response;

import lombok.Builder;

@Builder
public class CredentialsResponse {

    private String username;

    private String password;
}
