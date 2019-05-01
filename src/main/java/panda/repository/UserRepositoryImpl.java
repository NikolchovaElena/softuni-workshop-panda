package panda.repository;

import panda.domain.entities.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;
    private static final String FIND_USER_BY_ID_QUERY = "SELECT u FROM users u WHERE u.id=:id";
    private static final String FIND_USER_BY_USERNAME_QUERY = "SELECT u FROM users u WHERE u.username=:username";
    private static final String FIND_ALL_USERS_QUERY = "SELECT u FROM users u";
    private static final String COUNT_ALL_USERS_QUERY = "SELECT count(u) FROM users u";

    @Inject
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUsername(String username) {
        try {
            User user = this.entityManager
                    .createQuery(FIND_USER_BY_USERNAME_QUERY, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User save(User entity) {
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(entity);
        this.entityManager.getTransaction().commit();

        return entity;
    }

    @Override
    public User findById(String id) {
        return this.entityManager
                .createQuery(FIND_USER_BY_ID_QUERY, User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<User> findAll() {
        return this.entityManager
                .createQuery(FIND_ALL_USERS_QUERY, User.class)
                .getResultList();
    }

    @Override
    public Long size() {
        return this.entityManager
                .createQuery(COUNT_ALL_USERS_QUERY, Long.class)
                .getSingleResult();
    }
}
