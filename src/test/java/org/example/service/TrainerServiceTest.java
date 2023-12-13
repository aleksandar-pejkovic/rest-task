package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.example.dao.TrainerDAO;
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
                    .specialization(new TrainingType())
                    .build();

            doNothing().when(userAuthentication).authenticateUser(eq(trainer.getUsername()), eq(trainer.getPassword()));
        }
    }

    @Test
    void createTrainer() {
        // Arrange
        when(credentialsGenerator.generateUsername(any())).thenReturn("Valentino.Rossi");
        when(credentialsGenerator.generateRandomPassword()).thenReturn("9876543210");
        when(trainerDAO.saveTrainer(any())).thenReturn(trainer);

        // Act
        Trainer result = trainerService.createTrainer(trainer);

        // Assert
        verify(trainerDAO, times(1)).saveTrainer(trainer);
        assertEquals("Valentino.Rossi", result.getUsername());
        assertEquals("9876543210", result.getPassword());
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
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        // Act
        Trainer result = trainerService.changePassword(username, oldPassword, newPassword);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(username, oldPassword);
        verify(trainerDAO, times(1)).updateTrainer(trainer);
        assertEquals(newPassword, result.getPassword());
    }

    @Test
    void updateTrainer() {
        // Arrange
        when(trainerDAO.updateTrainer(trainer)).thenReturn(trainer);

        // Act
        trainerService.updateTrainer(trainer);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainer.getUsername(), trainer.getPassword());
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
        List<Trainer> result = trainerService.getNotAssignedTrainerList(traineeUsername, password);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(traineeUsername, password);
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