package ru.dictionary.repo.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;
import ru.dictionary.repo.UserRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static ru.dictionary.util.ValidationUtil.*;

@Repository
@Transactional(readOnly = true)
@Profile("jpa")
public class JPAUserRepo implements UserRepo {
    private static final Logger log = LoggerFactory.getLogger(JPAUserRepo.class);
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public User save(User user) {
        ValidationUtil.checkIsNew(user);
        log.info("save user {}", user);
        em.persist(user);
        return user;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        log.info("delete user with id{}", id);
        checkNotFoundWithId(
                em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0, id
        );
    }

    @Override
    public User get(Integer id) {
        log.info("get user with id{}", id);
        return checkNotFoundWithId(em.find(User.class, id), id);
    }

    @Override
    public List<User> getAll() {
        log.info("get all users");
        return em.createNamedQuery(User.GET_ALL, User.class).getResultList();
    }

    @Transactional
    @Override
    public User update(User user) {
        ValidationUtil.checkIsNotNew(user);
        log.info("update user {}", user);
        return em.merge(user);
    }

    @Override
    public User getByEmail(String email) {
        log.info("get user by email{}", email);
        return em.createNamedQuery(User.BY_EMAIL, User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}



