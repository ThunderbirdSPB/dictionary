package ru.dictionary.repo.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.dictionary.entity.User;
import ru.dictionary.repo.UserRepo;
import ru.dictionary.util.ValidationUtil;
import ru.dictionary.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static ru.dictionary.util.ValidationUtil.checkNotFoundWithId;

@Repository
@Profile("datajpa")
public class DataJpaUserRepo implements UserRepo {
    private static final Logger log = LoggerFactory.getLogger(DataJpaUserRepo.class);
    private final CrudUserRepo userRepo;

    public DataJpaUserRepo(CrudUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User save(User user) {
        ValidationUtil.checkIsNew(user);
        log.info("save user {}", user);
        return userRepo.save(user);
    }

    @Override
    public void delete(Integer id) {
        log.info("delete user with id={}", id);
        checkNotFoundWithId(userRepo.deleteByUserId(id) != 0, id);
    }

    @Override
    public User get(Integer id) {
        log.info("get user by id={}", id);
        return checkNotFoundWithId(userRepo.findById(id), id);
    }

    @Override
    public List<User> getAll() {
        log.info("get all users");
        return new ArrayList<>(userRepo.findAll());
    }

    @Override
    public User update(User user) {
        ValidationUtil.checkIsNotNew(user);
        log.info("update user {}", user);
        return userRepo.save(user);
    }

    @Override
    public User getByEmail(String email) {
        log.info("get user by email{}", email);
        return userRepo.getByEmail(email);
    }
}

