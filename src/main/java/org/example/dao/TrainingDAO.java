package org.example.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.model.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TrainingDAO extends AbstractDAO<Training> {

    public static final String ENTITY_ATTRIBUTE = "user";
    public static final String USER_ATTRIBUTE = "username";
    public static final String TRAINING_ATTRIBUTE_FOR_CRITERIA = "trainingDuration";

    @Autowired
    public TrainingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Training saveTraining(Training training) {
        return save(training);
    }

    public Training findById(long id) {
        Training training = findById(Training.class, id);
        if (Optional.ofNullable(training).isEmpty()) {
            log.error("Training not found by ID: {}", id);
            return new Training();
        }
        log.error("Training found by ID: {}", id);
        return training;
    }

    public List<Training> getTraineeTrainingList(String username, int trainingDuration) {
        return getTrainingList("trainee", username, trainingDuration);
    }

    public List<Training> getTrainerTrainingList(String username, int trainingDuration) {
        return getTrainingList("trainer", username, trainingDuration);
    }

    public Training updateTraining(Training training) {
        Training updatedTraining = update(training);
        log.info("Training updated successfully. ID: {}", updatedTraining.getId());
        return updatedTraining;
    }

    public boolean deleteTraining(Training training) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.merge(training);
            session.remove(training);
            log.info("Training deleted successfully. ID: {}", training.getId());
            return true;
        } catch (EntityNotFoundException e) {
            log.error("there was an error. Training was not deleted. ID: {}", training.getId(), e);
            return false;
        }
    }

    public List<Training> getAllTrainings() {
        List<Training> trainingList = findAll(Training.class);
        log.info("Retrieved all trainings. Count: {}", trainingList.size());
        return trainingList;
    }

    private List<Training> getTrainingList(String entityType, String username, int trainingDuration) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get(entityType).get(ENTITY_ATTRIBUTE).get(USER_ATTRIBUTE), username));
        predicates.add(criteriaBuilder.greaterThan(root.get(TRAINING_ATTRIBUTE_FOR_CRITERIA), trainingDuration));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
