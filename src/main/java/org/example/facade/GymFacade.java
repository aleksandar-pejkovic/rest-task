package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return traineeService.createTrainee(trainee);
    }

    public Trainee getTraineeByUsername(String username) {
        log.info("Getting trainee by USERNAME: {}", username);
        return traineeService.getTraineeByUsername(username);
    }

    public Trainee changeTraineePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing trainee password...");
        return traineeService.changePassword(username, oldPassword, newPassword);
    }

    public Trainee updateTrainee(Trainee trainee) {
        log.info("Updating trainee...");
        return traineeService.updateTrainee(trainee);
    }

    public Trainee activateTrainee(Trainee trainee) {
        log.info("Activating trainee...");
        return traineeService.activateTrainee(trainee);
    }

    public Trainee deactivateTrainee(Trainee trainee) {
        log.info("Deactivating trainee...");
        return traineeService.deactivateTrainee(trainee);
    }

    public void deleteTrainee(String username, String password) {
        log.info("Deleting trainee with USERNAME: {}", username);
        traineeService.deleteTrainee(username, password);
    }

    // Trainer-related methods

    public void readTrainers() {
        log.info("Reading trainers...");
        List<Trainer> trainers = trainerService.getAllTrainers();
    }

    public void createTrainer(Trainer trainer) {
        log.info("Creating trainer...");
        trainerService.createTrainer(trainer);
    }

    public Trainer getTrainerUsername(String username) {
        log.info("Getting trainer by Username: {}", username);
        return trainerService.getTrainerByUsername(username);
    }

    public Trainer changeTrainerPassword(String username, String oldPassword, String newPassword) {
        log.info("Changing trainer password...");
        return trainerService.changePassword(username, oldPassword, newPassword);
    }

    public void updateTrainer(Trainer trainer) {
        log.info("Updating trainer...");
        trainerService.updateTrainer(trainer);
    }

    public Trainer activateTrainer(Trainer trainer) {
        log.info("Activating trainer...");
        return trainerService.activateTrainer(trainer);
    }

    public Trainer deactivateTrainer(Trainer trainer) {
        log.info("Deactivating trainer...");
        return trainerService.deactivateTrainer(trainer);
    }

    public void deleteTrainer(String username, String password) {
        trainerService.deleteTrainer(username, password);
    }

    public List<Trainer> readNotAssignedTrainerList(String traineeUsername, String password) {
        return trainerService.getNotAssignedTrainerList(traineeUsername, password);
    }

    // Training-related methods

    public void readTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
    }

    public void createTraining(Training training) {
        log.info("Creating training...");
        trainingService.createTraining(training);
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

    public List<Training> readTraineeTrainingList(String username, String password, int trainingDuration) {
        return trainingService.getTraineeTrainingList(username, password, trainingDuration);
    }

    public List<Training> readTrainerTrainingList(String username, String password, int trainingDuration) {
        return trainingService.getTrainerTrainingList(username, password, trainingDuration);
    }
}
