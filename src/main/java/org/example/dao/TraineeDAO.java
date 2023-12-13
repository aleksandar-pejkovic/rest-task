package org.example.dao;

import java.util.List;

import org.example.model.Trainee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

    public Trainee findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<Trainee> query = session.createQuery("FROM Trainee t where t.user.username = :username", Trainee.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public Trainee updateTrainee(Trainee trainee) {
        return update(trainee);
    }

    public boolean deleteTraineeByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery("DELETE FROM Trainee t WHERE t.user.username = :username",
                Long.class);
        query.setParameter("username", username);

        int rowsDeleted = query.executeUpdate();

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
