package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserAuthentication {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserAuthentication(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void authenticateUser(String username, String password) {
        if (!authenticate(username, password)) {
            throw new RuntimeException("Authentication failed");
        } else {
            log.info("Successful user authentication");
        }
    }

    private Boolean authenticate(String username, String password) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM User WHERE username = :username AND password = :password", Long.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

            return query.uniqueResult() > 0;
        } catch (Exception e) {
            log.error("Authentication failed", e);
            return false;
        }
    }
}
