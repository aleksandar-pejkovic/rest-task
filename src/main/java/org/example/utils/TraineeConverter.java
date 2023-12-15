package org.example.utils;

import java.util.List;

import org.example.dto.trainee.TraineeDTO;
import org.example.dto.trainer.TrainerEmbeddedDTO;
import org.example.model.Trainee;

public class TraineeConverter {

    protected TraineeConverter() {
    }

    public static TraineeDTO convertToDto(Trainee entity) {
        List<TrainerEmbeddedDTO> trainerEmbeddedDTOList = TrainerConverter.convertToEmbeddedDtoList(entity.getTrainerList());

        return TraineeDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .firstName(entity.getUser().getFirstName())
                .lastName(entity.getUser().getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entity.getAddress())
                .isActive(entity.getUser().isActive())
                .trainerEmbeddedDTOList(trainerEmbeddedDTOList)
                .build();
    }
}
