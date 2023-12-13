package org.example;

import org.example.config.AppConfig;
import org.example.config.HibernateConfig;
import org.example.facade.GymFacade;
import org.example.model.Trainee;
import org.example.model.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class,
                HibernateConfig.class);

        User user = User.builder()
                .isActive(true)
                .lastName("Biaggi")
                .firstName("Max")
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address("11000 Belgrade")
                .dateOfBirth(new Date())
                .build();

        GymFacade gymFacade = context.getBean(GymFacade.class);
        gymFacade.createTrainee(trainee);
        gymFacade.readTrainees();
        gymFacade.readTrainers();
        gymFacade.readTrainings();
        gymFacade.readTraineeTrainingList("Max.Biaggi", "1234567890", 50);
        gymFacade.readNotAssignedTrainerList("Max.Biaggi", "1234567890");

        context.close();
    }
}
