package org.example.dao;

import java.util.List;

import org.example.model.Trainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TrainerDAO extends AbstractDAO<Trainer> {

    @Autowired
    public TrainerDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Trainer saveTrainer(Trainer trainer) {
        return save(trainer);
    }

    public Trainer findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trainer> query = session.createQuery("FROM Trainer t where t.user.username = :username", Trainer.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public Trainer updateTrainer(Trainer trainer) {
        return update(trainer);
    }

    public boolean deleteTrainerByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("DELETE FROM Trainer t WHERE t.user.username = :username",
                Long.class);
        query.setParameter("username", username);

        int rowsDeleted = query.executeUpdate();

        if (rowsDeleted < 1) {
            log.error("Trainer not found for USERNAME: {}", username);
            return false;
        } else {
            log.info("Trainer deleted successfully. USERNAME: {}", username);
            return true;
        }
    }

    public List<Trainer> getNotAssignedTrainers(String traineeUsername) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT t FROM Trainer t "
                + "LEFT JOIN t.traineeList te "
                + "WHERE te IS NULL OR te.user.username = :traineeUsername";

        Query<Trainer> query = session.createQuery(hql, Trainer.class);
        query.setParameter("traineeUsername", traineeUsername);

        List<Trainer> trainerList = query.getResultList();
        log.info("Successfully retrieved unassigned trainers list: {}", trainerList);
        return trainerList;
    }

    public List<Trainer> getAllTrainers() {
        return findAll(Trainer.class);
    }
}
