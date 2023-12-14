package org.example.utils;

import java.util.List;

import org.example.dto.TrainerDTO;
import org.example.model.Trainee;
import org.example.response.TraineeResponse;

public class TraineeConverter {

    protected TraineeConverter() {
    }

    public static TraineeResponse convertToResponse(Trainee entity) {
        List<TrainerDTO> trainerDTOList = TrainerConverter.convertToDtoList(entity.getTrainerList());

        return TraineeResponse.builder()
                .id(entity.getId())
                .firstName(entity.getUser().getFirstName())
                .lastName(entity.getUser().getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entity.getAddress())
                .isActive(entity.getUser().isActive())
                .trainerDTOList(trainerDTOList)
                .build();
    }
}
