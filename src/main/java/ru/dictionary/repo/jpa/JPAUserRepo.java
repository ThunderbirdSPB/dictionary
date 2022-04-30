package ru.dictionary.repo.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dictionary.entity.User;
import ru.dictionary.repo.UserRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import java.util.List;

import static ru.dictionary.util.ValidationUtil.*;

@Repository
@Transactional(readOnly = true)
public class JPAUserRepo implements UserRepo {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        boolean deleted = em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() != 0;
        checkNotFoundWithId(deleted, id);
    }

    @Override
    public User get(Integer id) {
        return checkNotFoundWithId(em.find(User.class, id), id);
    }

    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.GET_ALL, User.class).getResultList();
    }

    @Transactional
    @Override
    public User update(User user) {
        return em.merge(user);
    }

    public void test(){


    }

}



