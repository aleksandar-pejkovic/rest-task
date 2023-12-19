package org.example.facade;

import java.util.List;

import org.example.dto.credentials.CredentialsUpdateDTO;
import org.example.dto.trainee.TraineeUpdateDTO;
import org.example.dto.trainer.TrainerUpdateDTO;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TraineeService traineeService,
                     TrainerService trainerService,
                     TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    // Trainee-related methods

    public void readTrainees() {
        log.info("Reading trainees...");
        List<Trainee> trainees = traineeService.getAllTrainees();
    }

    public Trainee createTrainee(Trainee trainee) {
        log.info("Creating trainee...");
        return traineeService.createTrainee(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress()
        );
    }

    public Trainee getTraineeByUsername(String username) {
        log.info("Getting trainee by USERNAME: {}", username);
        return traineeService.getTraineeByUsername(username);
    }

    public Trainee changeTraineePassword(CredentialsUpdateDTO credentialsUpdateDTO) {
        log.info("Changing trainee password...");
        return traineeService.changePassword(credentialsUpdateDTO);
    }

    public Trainee updateTrainee(Trainee trainee) {
        TraineeUpdateDTO traineeUpdateDTO = TraineeUpdateDTO.builder()
                .username(trainee.getUsername())
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .isActive(trainee.getUser().isActive())
                .build();
        log.info("Updating trainee...");
        return traineeService.updateTrainee(traineeUpdateDTO);
    }

    public void deleteTrainee(String username) {
        log.info("Deleting trainee with USERNAME: {}", username);
        traineeService.deleteTrainee(username);
    }

    // Trainer-related methods

    public void readTrainers() {
        log.info("Reading trainers...");
        List<Trainer> trainers = trainerService.getAllTrainers();
    }

    public void createTrainer(Trainer trainer) {
        log.info("Creating trainer...");
        trainerService.createTrainer(trainer.getUser().getFirstName(), trainer.getUser().getLastName(),
                trainer.getSpecialization().getTrainingTypeName());
    }

    public Trainer getTrainerUsername(String username) {
        log.info("Getting trainer by Username: {}", username);
        return trainerService.getTrainerByUsername(username);
    }

    public Trainer changeTrainerPassword(CredentialsUpdateDTO credentialsUpdateDTO) {
        log.info("Changing trainer password...");
        return trainerService.changePassword(credentialsUpdateDTO);
    }

    public void updateTrainer(TrainerUpdateDTO trainerUpdateDTO) {
        log.info("Updating trainer...");
        trainerService.updateTrainer(trainerUpdateDTO);
    }

    public void deleteTrainer(String username, String password) {
        trainerService.deleteTrainer(username, password);
    }

    public List<Trainer> readNotAssignedTrainerList(String traineeUsername) {
        return trainerService.getNotAssignedTrainerList(traineeUsername);
    }

    // Training-related methods

    public void readTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
    }

    public Training getTrainingById(Long trainingId) {
        log.info("Getting training by ID: {}", trainingId);
        return trainingService.getTrainingById(trainingId);
    }

    public Training updateTraining(Training training) {
        log.info("Updating training...");
        return trainingService.updateTraining(training);
    }

    public boolean deleteTraining(Training training) {
        log.info("Deleting training with ID: {}", training.getId());
        return trainingService.deleteTraining(training);
    }
}
