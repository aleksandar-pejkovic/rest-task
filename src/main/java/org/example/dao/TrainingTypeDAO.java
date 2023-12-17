package org.example.dao;

import org.example.enums.TrainingTypeName;
import org.example.model.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

    public TrainingType findByTrainingTypeName(TrainingTypeName trainingTypeName) {
        Session session = sessionFactory.getCurrentSession();
        Query<TrainingType> query = session.createQuery("FROM TrainingType t where t.trainingTypeName = "
                + ":trainingTypeName", TrainingType.class);
        query.setParameter("trainingTypeName", trainingTypeName.name());
        return query.getSingleResult();
    }
}
