package org.example.service;

import java.util.List;

import org.example.dao.TraineeDAO;
import org.example.model.Trainee;
import org.example.utils.CredentialsGenerator;
import org.example.utils.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TraineeService {

    private final TraineeDAO traineeDAO;

    private final CredentialsGenerator generator;

    private final UserAuthentication authentication;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO, CredentialsGenerator credentialsGenerator, UserAuthentication authentication) {
        this.traineeDAO = traineeDAO;
        this.generator = credentialsGenerator;
        this.authentication = authentication;
    }

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        String username = generator.generateUsername(trainee.getUser());
        String password = generator.generateRandomPassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        return traineeDAO.saveTrainee(trainee);
    }

    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        Trainee trainee = traineeDAO.findByUsername(username);
        log.info("Retrieved Trainee by USERNAME {}: {}", username, trainee);
        return trainee;
    }

    @Transactional
    public Trainee changePassword(String username, String oldPassword, String newPassword) {
        authentication.authenticateUser(username, oldPassword);
        Trainee trainee = getTraineeByUsername(username);
        trainee.setPassword(newPassword);
        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);
        log.info("Password updated for trainee: {}", trainee);
        return updatedTrainee;
    }

    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        authentication.authenticateUser(trainee.getUsername(), trainee.getPassword());
        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);
        log.info("Trainee updated: {}", trainee);
        return updatedTrainee;
    }

    @Transactional
    public Trainee activateTrainee(Trainee trainee) {
        authentication.authenticateUser(trainee.getUsername(), trainee.getPassword());
        trainee.activateAccount();
        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);
        log.info("Activated account for trainee: {}", trainee);
        return updatedTrainee;
    }

    @Transactional
    public Trainee deactivateTrainee(Trainee trainee) {
        authentication.authenticateUser(trainee.getUsername(), trainee.getPassword());
        trainee.deactivateAccount();
        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);
        log.info("Deactivated account for trainee: {}", trainee);
        return updatedTrainee;
    }

    @Transactional
    public boolean deleteTrainee(String username, String password) {
        authentication.authenticateUser(username, password);
        return traineeDAO.deleteTraineeByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        List<Trainee> trainees = traineeDAO.getAllTrainees();
        log.info("Retrieved all Trainees: {}", trainees);
        return trainees;
    }
}
