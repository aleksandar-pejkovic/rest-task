package org.example.utils;

import java.util.List;

import org.example.dto.TrainerDTO;
import org.example.model.Trainer;

public class TrainerConverter {

    protected TrainerConverter() {
    }

    public static TrainerDTO convertToDto(Trainer trainer) {
        return TrainerDTO.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .build();
    }

    public static List<TrainerDTO> convertToDtoList(List<Trainer> trainers) {
        return trainers.stream()
                .map(TrainerConverter::convertToDto)
                .toList();
    }
}
