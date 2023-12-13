package org.example.config;

import org.example.dao.TraineeDAO;
import org.example.dao.TrainerDAO;
import org.example.dao.TrainingDAO;
import org.example.facade.GymFacade;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.example.utils.CredentialsGenerator;
import org.example.utils.UserAuthentication;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig {

    @Bean
    public CredentialsGenerator credentialsGenerator(SessionFactory sessionFactory) {
        return new CredentialsGenerator(sessionFactory);
    }

    @Bean
    public UserAuthentication userAuthentication(SessionFactory sessionFactory) {
        return new UserAuthentication(sessionFactory);
    }

    @Bean
    public TraineeDAO traineeDAO(SessionFactory sessionFactory) {
        return new TraineeDAO(sessionFactory);
    }

    @Bean
    public TrainerDAO trainerDAO(SessionFactory sessionFactory) {
        return new TrainerDAO(sessionFactory);
    }

    @Bean
    public TrainingDAO trainingDAO(SessionFactory sessionFactory) {
        return new TrainingDAO(sessionFactory);
    }

    @Bean
    public TraineeService traineeService(TraineeDAO traineeDAO, CredentialsGenerator credentialsGenerator,
                                         UserAuthentication authentication) {
        return new TraineeService(traineeDAO, credentialsGenerator, authentication);
    }

    @Bean
    public TrainerService trainerService(TrainerDAO trainerDAO, CredentialsGenerator credentialsGenerator,
                                         UserAuthentication authentication) {
        return new TrainerService(trainerDAO, credentialsGenerator, authentication);
    }

    @Bean
    public TrainingService trainingService(TrainingDAO trainingDAO, UserAuthentication authentication) {
        return new TrainingService(trainingDAO, authentication);
    }

    @Bean
    public GymFacade gymFacade(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService
    ) {
        return new GymFacade(traineeService, trainerService, trainingService);
    }
}
