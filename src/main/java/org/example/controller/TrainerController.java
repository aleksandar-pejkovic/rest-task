package org.example.controller;

import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.User;
import org.example.response.CredentialsResponse;
import org.example.service.TrainerService;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/trainers", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class TrainerController {

    private final TrainerService trainerService;

    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainerController(TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @PostMapping
    public CredentialsResponse traineeRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam long specializationId
    ) {
        User newUser = buildNewUser(firstName, lastName);

        TrainingType specialization = trainingTypeService.findTrainingTypeById(specializationId);

        Trainer newTrainer = buildNewTrainer(newUser, specialization);

        Trainer savedTrainer = trainerService.createTrainer(newTrainer);

        return CredentialsResponse.builder()
                .username(savedTrainer.getUsername())
                .password(savedTrainer.getPassword())
                .build();
    }

    private User buildNewUser(String firstName, String lastName) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private Trainer buildNewTrainer(User newUser, TrainingType specialization) {
        return Trainer.builder()
                .user(newUser)
                .specialization(specialization)
                .build();
    }
}
