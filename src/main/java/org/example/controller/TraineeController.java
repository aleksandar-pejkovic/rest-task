package org.example.controller;

import java.util.Date;

import org.example.model.Trainee;
import org.example.model.User;
import org.example.response.CredentialsResponse;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/trainees", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class TraineeController {

    private final TraineeService traineeService;

    @Autowired
    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    public CredentialsResponse traineeRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) Date dateOfBirth,
            @RequestParam(required = false) String address
    ) {
        User newUser = buildNewUser(firstName, lastName);

        Trainee newTrainee = buildNewTrainee(dateOfBirth, address, newUser);

        Trainee savedTrainee = traineeService.createTrainee(newTrainee);

        return CredentialsResponse.builder()
                .username(savedTrainee.getUsername())
                .password(savedTrainee.getPassword())
                .build();
    }

    @PutMapping("/change-login")
    public ResponseEntity<Boolean> changeLogin(
            @RequestParam String username,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        Trainee traineeAfterUpdate = traineeService.changePassword(username, oldPassword, newPassword);
        if (newPassword.equals(traineeAfterUpdate.getPassword())) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    private User buildNewUser(String firstName, String lastName) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    private Trainee buildNewTrainee(Date dateOfBirth, String address, User newUser) {
        return Trainee.builder()
                .dateOfBirth(dateOfBirth)
                .address(address)
                .user(newUser)
                .build();
    }
}
