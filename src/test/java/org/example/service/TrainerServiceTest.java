package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.example.dao.TrainerDAO;
import org.example.dao.TrainingDAO;
import org.example.dto.credentials.CredentialsUpdateDTO;
import org.example.dto.trainer.TrainerUpdateDTO;
import org.example.enums.TrainingTypeName;
import org.example.model.Trainer;
import org.example.model.TrainingType;
import org.example.model.User;
import org.example.utils.CredentialsGenerator;
import org.example.utils.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Mock
    private UserAuthentication userAuthentication;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this)) {
            User user = User.builder()
                    .isActive(true)
                    .lastName("Rossi")
                    .firstName("Valentino")
                    .username("Valentino.Rossi")
                    .password("9876543210")
                    .build();

            trainer = Trainer.builder()
                    .user(user)
                    .specialization(TrainingType.builder()
                            .id(1L)
                            .trainingTypeName(TrainingTypeName.AEROBIC)
                            .build())
                    .build();

            when(userAuthentication.authenticateUser(anyString(), anyString())).thenReturn(true);
        }
    }

    @Test
    void createTrainer() {
        // Arrange
        TrainingType trainingType = TrainingType.builder()
                .id(1L)
                .trainingTypeName(TrainingTypeName.AEROBIC)
                .build();

        when(trainingDAO.findTrainingTypeByName(any())).thenReturn(trainingType);
        when(credentialsGenerator.generateUsername(any())).thenReturn("Valentino.Rossi");
        when(credentialsGenerator.generateRandomPassword()).thenReturn("9876543210");
        when(trainerDAO.saveTrainer(any())).thenReturn(trainer);

        // Act
        Trainer result = trainerService.createTrainer(trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(), trainer.getSpecialization().getTrainingTypeName());

        // Assert
        verify(trainerDAO, times(1)).saveTrainer(any());
    }

    @Test
    void getTrainerByUsername() {
        // Arrange
        String username = "testUser";
        Trainer expectedTrainer = new Trainer();
        when(trainerDAO.findByUsername(username)).thenReturn(expectedTrainer);

        // Act
        Trainer result = trainerService.getTrainerByUsername(username);

        // Assert
        verify(trainerDAO, times(1)).findByUsername(username);
        assertEquals(expectedTrainer, result);
    }

    @Test
    void changePassword() {
        // Arrange
        CredentialsUpdateDTO credentialsUpdateDTO = CredentialsUpdateDTO.builder()
                .username("testUser")
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        when(trainerService.getTrainerByUsername(credentialsUpdateDTO.getUsername())).thenReturn(trainer);
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        // Act
        Trainer result = trainerService.changePassword(credentialsUpdateDTO);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(credentialsUpdateDTO.getUsername(),
                credentialsUpdateDTO.getOldPassword());
        verify(trainerDAO, times(1)).updateTrainer(trainer);
        assertEquals(credentialsUpdateDTO.getNewPassword(), result.getPassword());
    }

    @Test
    void updateTrainer() {
        // Arrange
        TrainingType trainingType = TrainingType.builder()
                .id(1L)
                .trainingTypeName(TrainingTypeName.AEROBIC)
                .build();

        when(trainerDAO.findByUsername(any())).thenReturn(trainer);
        when(trainingDAO.findTrainingTypeByName(any())).thenReturn(trainingType);
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);
        TrainerUpdateDTO trainerUpdateDTO = TrainerUpdateDTO.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .isActive(trainer.getUser().isActive())
                .build();

        // Act
        trainerService.updateTrainer(trainerUpdateDTO);

        // Assert
        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }

    @Test
    void activateTrainer() {
        // Arrange
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        // Act
        Trainer result = trainerService.activateTrainer(trainer);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainer.getUsername(), trainer.getPassword());
        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }

    @Test
    void deactivateTrainer() {
        // Arrange
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        // Act
        Trainer result = trainerService.deactivateTrainer(trainer);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainer.getUsername(), trainer.getPassword());
        verify(trainerDAO, times(1)).updateTrainer(trainer);
    }

    @Test
    void deleteTrainer() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        when(trainerDAO.deleteTrainerByUsername(username)).thenReturn(true);

        // Act
        boolean result = trainerService.deleteTrainer(username, password);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(username, password);
        verify(trainerDAO, times(1)).deleteTrainerByUsername(username);
        assertTrue(result);
    }

    @Test
    void getNotAssignedTrainerList() {
        // Arrange
        String traineeUsername = "traineeUser";
        String password = "testPassword";
        List<Trainer> expectedTrainers = Collections.singletonList(new Trainer());
        when(trainerDAO.getNotAssignedTrainers(traineeUsername)).thenReturn(expectedTrainers);

        // Act
        List<Trainer> result = trainerService.getNotAssignedTrainerList(traineeUsername);

        // Assert
        verify(trainerDAO, times(1)).getNotAssignedTrainers(traineeUsername);
        assertEquals(expectedTrainers, result);
    }

    @Test
    void getAllTrainers() {
        // Arrange
        List<Trainer> expectedTrainers = Collections.singletonList(new Trainer());
        when(trainerDAO.getAllTrainers()).thenReturn(expectedTrainers);

        // Act
        List<Trainer> result = trainerService.getAllTrainers();

        // Assert
        verify(trainerDAO, times(1)).getAllTrainers();
        assertEquals(expectedTrainers, result);
    }

}