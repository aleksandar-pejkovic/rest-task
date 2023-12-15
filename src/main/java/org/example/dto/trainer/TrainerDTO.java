package org.example.dto.trainer;

import java.util.List;

import org.example.dto.trainee.TraineeEmbeddedDTO;

import lombok.Builder;

@Builder
public class TrainerDTO {

    String username;

    String firstName;

    String lastName;

    String specialization;

    boolean isActive;

    List<TraineeEmbeddedDTO> traineeEmbeddedDTOList;
}
