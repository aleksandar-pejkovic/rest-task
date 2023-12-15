package org.example.controller;

import java.util.Date;

import org.example.dto.credentials.CredentialsDTO;
import org.example.dto.trainee.TraineeDTO;
import org.example.model.Trainee;
import org.example.service.TraineeService;
import org.example.utils.TraineeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public CredentialsDTO traineeRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) Date dateOfBirth,
            @RequestParam(required = false) String address
    ) {
        Trainee savedTrainee = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);

        return CredentialsDTO.builder()
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

    @GetMapping("/{username}")
    public TraineeDTO getTraineeByUsername(@PathVariable String username) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        return TraineeConverter.convertToDto(trainee);
    }

    @PutMapping
    public TraineeDTO updateTraineeProfile(
            @RequestParam String username,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) Date dateOfBirth,
            @RequestParam(required = false) String address,
            @RequestParam boolean isActive
    ) {
        Trainee trainee = traineeService.getTraineeByUsername(username);
        trainee.getUser().setFirstName(firstName);
        trainee.getUser().setLastName(lastName);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        trainee.getUser().setActive(isActive);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);
        return TraineeConverter.convertToDto(updatedTrainee);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteTraineeProfile(@RequestParam String username) {
        boolean successfulDeletion = traineeService.deleteTrainee(username);
        if (successfulDeletion) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
