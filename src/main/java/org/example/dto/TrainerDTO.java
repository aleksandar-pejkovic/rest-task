package org.example.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerDTO {

    String username;

    String firstName;

    String lastName;

    String specialization;
}
