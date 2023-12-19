package org.example.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.example.enums.TrainingTypeName;
import org.example.model.Training;
import org.example.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

    public static final String TRAINEE_TYPE = "trainee";
    public static final String TRAINER_TYPE = "trainer";
    public static final String USER_ATTRIBUTE = "user";
    public static final String USERNAME_ATTRIBUTE = "username";
    public static final String TRAINING_DATE_ATTRIBUTE_FOR_CRITERIA = "trainingDate";
    public static final String TRAINING_TYPE_ATTRIBUTE_FOR_CRITERIA = "trainingType";
    public static final String TRAINING_TYPE_NAME_ATTRIBUTE_FOR_CRITERIA = "trainingTypeName";

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

    public List<Training> getTraineeTrainingList(String username,
                                                 Date periodFrom,
                                                 Date periodTo,
                                                 String trainerName,
                                                 String trainingTypeName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(
                criteriaBuilder
                        .equal(
                                root.get(TRAINEE_TYPE)
                                        .get(USER_ATTRIBUTE)
                                        .get(USERNAME_ATTRIBUTE),
                                username)
        );
        predicates.add(
                criteriaBuilder
                        .greaterThan(
                                root.get(TRAINING_DATE_ATTRIBUTE_FOR_CRITERIA),
                                periodFrom)
        );
        predicates.add(
                criteriaBuilder
                        .lessThan(
                                root.get(TRAINING_DATE_ATTRIBUTE_FOR_CRITERIA),
                                periodTo)
        );
        predicates.add(
                criteriaBuilder
                        .equal(
                                root.get(TRAINER_TYPE)
                                        .get(USER_ATTRIBUTE)
                                        .get(USERNAME_ATTRIBUTE),
                                trainerName));
        predicates.add(
                criteriaBuilder
                        .equal(
                                root.get(TRAINING_TYPE_ATTRIBUTE_FOR_CRITERIA)
                                        .get(TRAINING_TYPE_NAME_ATTRIBUTE_FOR_CRITERIA),
                                trainingTypeName));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return session.createQuery(criteriaQuery).getResultList();
    }

    public List<Training> getTrainerTrainingList(String username,
                                                 Date periodFrom,
                                                 Date periodTo,
                                                 String traineeName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Training> criteriaQuery = criteriaBuilder.createQuery(Training.class);
        Root<Training> root = criteriaQuery.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(
                criteriaBuilder
                        .equal(
                                root.get(TRAINER_TYPE)
                                        .get(USER_ATTRIBUTE)
                                        .get(USERNAME_ATTRIBUTE),
                                username)
        );
        predicates.add(
                criteriaBuilder
                        .greaterThan(
                                root.get(TRAINING_DATE_ATTRIBUTE_FOR_CRITERIA),
                                periodFrom)
        );
        predicates.add(
                criteriaBuilder
                        .lessThan(
                                root.get(TRAINING_DATE_ATTRIBUTE_FOR_CRITERIA),
                                periodTo)
        );
        predicates.add(
                criteriaBuilder
                        .equal(
                                root.get(TRAINEE_TYPE)
                                        .get(USER_ATTRIBUTE)
                                        .get(USERNAME_ATTRIBUTE),
                                traineeName));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return session.createQuery(criteriaQuery).getResultList();
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

    public List<Training> findAllTrainings() {
        List<Training> trainingList = findAll(Training.class);
        log.info("Retrieved all trainings. Count: {}", trainingList.size());
        return trainingList;
    }

    public TrainingType findTrainingTypeByName(TrainingTypeName trainingTypeName) {
        Session session = sessionFactory.getCurrentSession();
        Query<TrainingType> query = session.createQuery("FROM TrainingType t where t.trainingTypeName = "
                + ":trainingTypeName", TrainingType.class);
        query.setParameter("trainingTypeName", trainingTypeName.name());
        return query.getSingleResult();
    }

    public List<TrainingType> findAllTrainingTypes() {
        Session session = sessionFactory.getCurrentSession();
        Query<TrainingType> query = session.createQuery("FROM TrainingType", TrainingType.class);
        return query.getResultList();
    }
}
