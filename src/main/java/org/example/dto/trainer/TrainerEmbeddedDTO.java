package org.example.dto.trainer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerEmbeddedDTO {

    String username;

    String firstName;

    String lastName;

    String specialization;
}
