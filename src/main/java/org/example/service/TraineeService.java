package org.example.service;

import java.util.Date;
import java.util.List;

import org.example.dao.TraineeDAO;
import org.example.dto.credentials.CredentialsUpdateDTO;
import org.example.dto.trainee.TraineeUpdateDTO;
import org.example.model.Trainee;
import org.example.model.User;
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
    public Trainee createTrainee(String firstName, String lastName, Date dateOfBirth, String address) {
        User newUser = buildNewUser(firstName, lastName);
        Trainee newTrainee = buildNewTrainee(dateOfBirth, address, newUser);
        String username = generator.generateUsername(newTrainee.getUser());
        String password = generator.generateRandomPassword();
        newTrainee.setUsername(username);
        newTrainee.setPassword(password);
        return traineeDAO.saveTrainee(newTrainee);
    }

    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        Trainee trainee = traineeDAO.findTraineeByUsername(username);
        log.info("Retrieved Trainee by USERNAME {}: {}", username, trainee);
        return trainee;
    }

    @Transactional
    public Trainee changePassword(CredentialsUpdateDTO credentialsUpdateDTO) {
        authentication.authenticateUser(credentialsUpdateDTO.getUsername(), credentialsUpdateDTO.getOldPassword());
        Trainee trainee = getTraineeByUsername(credentialsUpdateDTO.getUsername());
        trainee.setPassword(credentialsUpdateDTO.getNewPassword());
        Trainee updatedTrainee = traineeDAO.updateTrainee(trainee);
        log.info("Password updated for trainee: {}", trainee);
        return updatedTrainee;
    }

    @Transactional
    public Trainee updateTrainee(TraineeUpdateDTO traineeUpdateDTO) {
        Trainee trainee = getTraineeByUsername(traineeUpdateDTO.getUsername());
        trainee.getUser().setFirstName(traineeUpdateDTO.getFirstName());
        trainee.getUser().setLastName(traineeUpdateDTO.getLastName());
        trainee.setDateOfBirth(traineeUpdateDTO.getDateOfBirth());
        trainee.setAddress(traineeUpdateDTO.getAddress());
        trainee.getUser().setActive(traineeUpdateDTO.isActive());
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
    public boolean deleteTrainee(String username) {
        return traineeDAO.deleteTraineeByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        List<Trainee> trainees = traineeDAO.getAllTrainees();
        log.info("Retrieved all Trainees: {}", trainees);
        return trainees;
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
