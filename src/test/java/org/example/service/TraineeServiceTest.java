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
import java.util.Date;
import java.util.List;

import org.example.dao.TraineeDAO;
import org.example.model.Trainee;
import org.example.model.User;
import org.example.utils.CredentialsGenerator;
import org.example.utils.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private CredentialsGenerator credentialsGenerator;

    @Mock
    private UserAuthentication userAuthentication;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() throws Exception {
        try (AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this)) {
            User user = User.builder()
                    .isActive(true)
                    .lastName("Biaggi")
                    .firstName("Max")
                    .username("Max.Biaggi")
                    .password("0123456789")
                    .build();

            trainee = Trainee.builder()
                    .user(user)
                    .address("11000 Belgrade")
                    .dateOfBirth(new Date())
                    .build();

            doNothing().when(userAuthentication).authenticateUser(eq(trainee.getUsername()), eq(trainee.getPassword()));
        }
    }

    @Test
    void createTrainee() {
        // Arrange
        when(credentialsGenerator.generateUsername(any())).thenReturn("Max.Biaggi");
        when(credentialsGenerator.generateRandomPassword()).thenReturn("0123456789");
        when(traineeDAO.saveTrainee(any())).thenReturn(trainee);

        // Act
        Trainee result = traineeService.createTrainee(trainee);

        // Assert
        verify(traineeDAO, times(1)).saveTrainee(trainee);
        assertEquals("Max.Biaggi", result.getUsername());
        assertEquals("0123456789", result.getPassword());
    }

    @Test
    void getTraineeByUsername() {
        // Arrange
        String username = "testUser";
        Trainee expectedTrainee = new Trainee();
        when(traineeDAO.findByUsername(username)).thenReturn(expectedTrainee);

        // Act
        Trainee result = traineeService.getTraineeByUsername(username);

        // Assert
        verify(traineeDAO, times(1)).findByUsername(username);
        assertEquals(expectedTrainee, result);
    }

    @Test
    void changePassword() {
        // Arrange
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(traineeService.getTraineeByUsername(username)).thenReturn(trainee);
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeService.changePassword(username, oldPassword, newPassword);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(username, oldPassword);
        verify(traineeDAO, times(1)).updateTrainee(trainee);
        assertEquals(newPassword, result.getPassword());
    }

    @Test
    void updateTrainee() {
        // Arrange
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeService.updateTrainee(trainee);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainee.getUsername(), trainee.getPassword());
        verify(traineeDAO, times(1)).updateTrainee(trainee);
        assertEquals(trainee, result);
    }

    @Test
    void activateTrainee() {
        // Arrange
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeService.activateTrainee(trainee);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainee.getUsername(), trainee.getPassword());
        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }

    @Test
    void deactivateTrainee() {
        // Arrange
        when(traineeDAO.updateTrainee(trainee)).thenReturn(trainee);

        // Act
        Trainee result = traineeService.deactivateTrainee(trainee);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(trainee.getUsername(), trainee.getPassword());
        verify(traineeDAO, times(1)).updateTrainee(trainee);
    }

    @Test
    void deleteTrainee() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        when(traineeDAO.deleteTraineeByUsername(username)).thenReturn(true);

        // Act
        boolean result = traineeService.deleteTrainee(username, password);

        // Assert
        verify(userAuthentication, times(1)).authenticateUser(username, password);
        verify(traineeDAO, times(1)).deleteTraineeByUsername(username);
        assertTrue(result);
    }

    @Test
    void getAllTrainees() {
        // Arrange
        List<Trainee> expectedTrainees = Collections.singletonList(new Trainee());
        when(traineeDAO.getAllTrainees()).thenReturn(expectedTrainees);

        // Act
        List<Trainee> result = traineeService.getAllTrainees();

        // Assert
        verify(traineeDAO, times(1)).getAllTrainees();
        assertEquals(expectedTrainees, result);
    }
}
