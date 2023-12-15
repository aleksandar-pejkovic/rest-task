package org.example.controller;

import org.example.enums.TrainingTypeName;
import org.example.model.Trainer;
import org.example.response.CredentialsResponse;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public CredentialsResponse traineeRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam TrainingTypeName specialization
    ) {
        Trainer savedTrainer = trainerService.createTrainer(firstName, lastName, specialization);

        return CredentialsResponse.builder()
                .username(savedTrainer.getUsername())
                .password(savedTrainer.getPassword())
                .build();
    }

    @PutMapping("/change-login")
    public ResponseEntity<Boolean> changeLogin(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        Trainer trainerAfterUpdate = trainerService.changePassword(username, oldPassword, newPassword);
        if (newPassword.equals(trainerAfterUpdate.getPassword())) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
