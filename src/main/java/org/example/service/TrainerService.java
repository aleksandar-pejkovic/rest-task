package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.dao.TraineeDAO;
import org.example.dao.TrainerDAO;
import org.example.dao.TrainingDAO;
import org.example.dto.credentials.CredentialsUpdateDTO;
import org.example.dto.trainer.TrainerListDTO;
import org.example.dto.trainer.TrainerUpdateDTO;
import org.example.enums.TrainingTypeName;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.User;
import org.example.utils.CredentialsGenerator;
import org.example.utils.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainerService {

    private final TrainerDAO trainerDAO;

    private final TraineeDAO traineeDAO;

    private final CredentialsGenerator generator;

    private final UserAuthentication authentication;

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO, TraineeDAO traineeDAO, CredentialsGenerator credentialsGenerator, UserAuthentication authentication, TrainingDAO trainingDAO) {
        this.trainerDAO = trainerDAO;
        this.traineeDAO = traineeDAO;
        this.generator = credentialsGenerator;
        this.authentication = authentication;
        this.trainingDAO = trainingDAO;
    }

    @Transactional
    public Trainer createTrainer(String firstName, String lastName, TrainingTypeName specialization) {
        TrainingType trainingType = trainingDAO.findTrainingTypeByName(specialization);
        User newUser = buildNewUser(firstName, lastName);
        Trainer newTrainer = buildNewTrainer(newUser, trainingType);
        String username = generator.generateUsername(newTrainer.getUser());
        String password = generator.generateRandomPassword();
        newTrainer.setUsername(username);
        newTrainer.setPassword(password);
        return trainerDAO.saveTrainer(newTrainer);
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username) {
        Trainer trainer = trainerDAO.findTrainerByUsername(username);
        log.info("Retrieved Trainer by USERNAME {}: {}", username, trainer);
        return trainer;
    }

    @Transactional
    public Trainer changePassword(CredentialsUpdateDTO credentialsUpdateDTO) {
        authentication.authenticateUser(credentialsUpdateDTO.getUsername(), credentialsUpdateDTO.getOldPassword());
        Trainer trainer = getTrainerByUsername(credentialsUpdateDTO.getUsername());
        trainer.setPassword(credentialsUpdateDTO.getNewPassword());
        Trainer updatedTrainer = trainerDAO.updateTrainer(trainer);
        log.info("Password updated for trainer: {}", trainer);
        return updatedTrainer;
    }

    @Transactional
    public Trainer updateTrainer(TrainerUpdateDTO trainerUpdateDTO) {
        Trainer trainer = getTrainerByUsername(trainerUpdateDTO.getUsername());
        TrainingType trainingType = trainingDAO.findTrainingTypeByName(trainerUpdateDTO.getSpecialization());
        trainer.getUser().setFirstName(trainerUpdateDTO.getFirstName());
        trainer.getUser().setLastName(trainerUpdateDTO.getLastName());
        trainer.setSpecialization(trainingType);
        trainer.getUser().setActive(trainerUpdateDTO.isActive());
        Trainer updatedTrainer = trainerDAO.updateTrainer(trainer);
        log.info("Trainer updated: {}", updatedTrainer);
        return updatedTrainer;
    }

    public boolean toggleTrainerActivation(String username, boolean isActive) {
        Trainer trainer = trainerDAO.findTrainerByUsername(username);
        trainer.getUser().setActive(isActive);
        Trainer updatedTrainer = trainerDAO.updateTrainer(trainer);
        log.info("Active account status for TRAINER: {}, has been set to: {}", username, isActive);
        return Optional.ofNullable(updatedTrainer).isPresent();
    }

    @Transactional
    public boolean deleteTrainer(String username, String password) {
        authentication.authenticateUser(username, password);
        log.info("Deleting trainer with USERNAME: {}", username);
        return trainerDAO.deleteTrainerByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getNotAssignedTrainerList(String traineeUsername) {
        log.info("Retrieving trainer list for trainee with USERNAME: {}", traineeUsername);
        return trainerDAO.getNotAssignedTrainers(traineeUsername);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = trainerDAO.getAllTrainers();
        log.info("Retrieved all Trainers: {}", trainers);
        return trainers;
    }

    @Transactional
    public List<Trainer> updateTraineeTrainerList(String traineeUsername, TrainerListDTO trainerListDTO) {
        Trainee trainee = traineeDAO.findTraineeByUsername(traineeUsername);
        List<Trainer> trainers = trainerDAO.getAllTrainers().stream()
                .filter(trainer -> trainerListDTO.getTrainerUsernameList().contains(trainer.getUsername()))
                .toList();
        trainee.getTrainerList().addAll(trainers);
        traineeDAO.updateTrainee(trainee);
        return trainee.getTrainerList();
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
