package org.example.dao;

import java.util.Optional;

import org.example.model.Training;
import org.example.model.TrainingType;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TrainingTypeDAO extends AbstractDAO<TrainingType> {

    @Autowired
    public TrainingTypeDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public TrainingType findTrainingTypeById(long id) {
        TrainingType trainingType = findById(TrainingType.class, id);
        if (Optional.ofNullable(trainingType).isEmpty()) {
            log.error("TrainingType not found by ID: {}", id);
            return new TrainingType();
        }
        log.error("TrainingType found by ID: {}", id);
        return trainingType;
    }
}
