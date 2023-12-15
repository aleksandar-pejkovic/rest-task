package org.example.dto.trainer;

import org.example.enums.TrainingTypeName;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerUpdateDTO {

    String username;

    String firstName;

    String lastName;

    TrainingTypeName specialization;

    boolean isActive;
}
