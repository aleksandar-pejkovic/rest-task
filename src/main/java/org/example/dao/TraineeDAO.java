package org.example.dao;

import java.util.List;

import org.example.model.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TraineeDAO extends AbstractDAO<Trainee> {

    @Autowired
    public TraineeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Trainee saveTrainee(Trainee trainee) {
        return save(trainee);
    }

    public Trainee findTraineeByUsername(String username) {
        return findByUsername(username, Trainee.class);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return update(trainee);
    }

    public boolean deleteTraineeByUsername(String username) {
        int rowsDeleted = deleteByUsername(username, Trainee.class);

        if (rowsDeleted < 1) {
            log.error("Trainee not found for USERNAME: {}", username);
            return false;
        } else {
            log.info("Trainee deleted successfully. USERNAME: {}", username);
            return true;
        }
    }

    public List<Trainee> getAllTrainees() {
        return findAll(Trainee.class);
    }
}
