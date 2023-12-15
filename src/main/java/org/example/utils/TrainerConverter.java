package org.example.utils;

import java.util.List;

import org.example.dto.trainer.TrainerEmbeddedDTO;
import org.example.model.Trainer;

public class TrainerConverter {

    protected TrainerConverter() {
    }

    public static TrainerEmbeddedDTO convertToDto(Trainer trainer) {
        return TrainerEmbeddedDTO.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName().name())
                .build();
    }

    public static List<TrainerEmbeddedDTO> convertToEmbeddedDtoList(List<Trainer> trainers) {
        return trainers.stream()
                .map(TrainerConverter::convertToDto)
                .toList();
    }
}
