package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.dao.TraineeDAO;
import org.example.dao.TrainerDAO;
import org.example.dao.TrainingDAO;
import org.example.dto.training.TrainingCreateDTO;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainingService {

    private final TrainingDAO trainingDAO;

    private final TraineeDAO traineeDAO;

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO, TraineeDAO traineeDao, TrainerDAO trainerDAO) {
        this.trainingDAO = trainingDAO;
        this.traineeDAO = traineeDao;
        this.trainerDAO = trainerDAO;
    }

    @Transactional
    public boolean createTraining(TrainingCreateDTO trainingCreateDTO) {

        Trainee trainee = traineeDAO.findTraineeByUsername(trainingCreateDTO.getTraineeUsername());
        Trainer trainer = trainerDAO.findTrainerByUsername(trainingCreateDTO.getTrainerUsername());
        trainer.getTraineeList().add(trainee);
        trainee.getTrainerList().add(trainer);

        TrainingType trainingType = trainingDAO.findTrainingTypeByName(trainingCreateDTO.getTrainingTypeName());

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(trainingType.getTrainingTypeName().name())
                .trainingType(trainingType)
                .trainingDate(trainingCreateDTO.getTrainingDate())
                .trainingDuration(trainingCreateDTO.getTrainingDuration())
                .build();

        Training savedTraining = trainingDAO.saveTraining(training);

        return Optional.ofNullable(savedTraining).isPresent();
    }

    @Transactional(readOnly = true)
    public Training getTrainingById(long id) {
        Training training = trainingDAO.findById(id);
        log.info("Retrieved Training by ID {}: {}", id, training);
        return training;
    }

    @Transactional
    public Training updateTraining(Training training) {
        Training updatedTraining = trainingDAO.updateTraining(training);
        log.info("Training updated: {}", training);
        return updatedTraining;
    }

    @Transactional
    public boolean deleteTraining(Training training) {
        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();
        trainer.getTraineeList().remove(trainee);
        trainee.getTrainerList().remove(trainer);
        boolean result = trainingDAO.deleteTraining(training);
        log.info("Training deleted with ID: {}", training.getId());
        return result;
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainingList(String username, int trainingDuration) {
        log.info("Retrieving training list for trainee with USERNAME: {}", username);
        List<Training> trainingList = trainingDAO.getTraineeTrainingList(username, trainingDuration);
        log.info("Successfully retrieved training list: {}", trainingList);
        return trainingList;
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainingList(String username, int trainingDuration) {
        log.info("Retrieving training list for trainer with USERNAME: {}", username);
        List<Training> trainingList = trainingDAO.getTrainerTrainingList(username, trainingDuration);
        log.info("Successfully retrieved training list: {}", trainingList);
        return trainingList;
    }

    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        log.info("Reading trainings...");
        List<Training> trainings = trainingDAO.getAllTrainings();
        log.info("Retrieved all Trainings: {}", trainings);
        return trainings;
    }
}
