package org.example.controller;

import java.util.List;

import org.example.dto.credentials.CredentialsDTO;
import org.example.dto.credentials.CredentialsUpdateDTO;
import org.example.dto.trainer.TrainerDTO;
import org.example.dto.trainer.TrainerEmbeddedDTO;
import org.example.dto.trainer.TrainerListDTO;
import org.example.dto.trainer.TrainerUpdateDTO;
import org.example.enums.TrainingTypeName;
import org.example.model.Trainer;
import org.example.service.TrainerService;
import org.example.utils.TrainerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/trainers", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public CredentialsDTO traineeRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam TrainingTypeName specialization
    ) {
        Trainer savedTrainer = trainerService.createTrainer(firstName, lastName, specialization);

        return CredentialsDTO.builder()
                .username(savedTrainer.getUsername())
                .password(savedTrainer.getPassword())
                .build();
    }

    @PutMapping("/change-login")
    public ResponseEntity<Boolean> changeLogin(@RequestBody CredentialsUpdateDTO credentialsUpdateDTO) {
        Trainer trainerAfterUpdate = trainerService.changePassword(credentialsUpdateDTO);
        return (credentialsUpdateDTO.getNewPassword().equals(trainerAfterUpdate.getPassword()))
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }

    @GetMapping
    public TrainerDTO getTrainerByUsername(@RequestParam String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return TrainerConverter.convertToDto(trainer);
    }

    @PutMapping
    public TrainerDTO updateTraineeProfile(@RequestBody TrainerUpdateDTO trainerUpdateDTO) {
        Trainer updatedTrainer = trainerService.updateTrainer(trainerUpdateDTO);
        return TrainerConverter.convertToDto(updatedTrainer);
    }

    @GetMapping("/unassigned")
    public List<TrainerEmbeddedDTO> getNotAssignedOnTrainee(@RequestParam String traineeUsername) {
        List<Trainer> unassignedTrainers = trainerService.getNotAssignedTrainerList(traineeUsername);
        return TrainerConverter.convertToEmbeddedDtoList(unassignedTrainers);
    }

    @PutMapping("/{traineeUsername}/updateTrainers")
    public List<TrainerEmbeddedDTO> updateTraineeTrainerList(
            @PathVariable String traineeUsername,
            @RequestBody TrainerListDTO trainerListDTO
    ) {
        List<Trainer> updatedTraineeTrainerList = trainerService.updateTraineeTrainerList(traineeUsername, trainerListDTO);
        return TrainerConverter.convertToEmbeddedDtoList(updatedTraineeTrainerList);
    }

    @PatchMapping
    public ResponseEntity<Boolean> toggleTraineeActivation(@RequestParam String username,
                                                           @RequestParam boolean isActive) {
        boolean successfulRequest = trainerService.toggleTrainerActivation(username, isActive);
        return successfulRequest
                ? ResponseEntity.ok(true)
                : ResponseEntity.badRequest().body(false);
    }
}
