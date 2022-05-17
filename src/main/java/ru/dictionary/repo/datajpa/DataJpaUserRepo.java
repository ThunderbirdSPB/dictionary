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
        log.info("save user {}", user);
        return userRepo.save(user);
    }

    @Override
    public void delete(Integer id) {
        log.info("delete user with id={}", id);
        userRepo.deleteById(id);
    }

    @Override
    public User get(Integer id) {
        log.info("get user by id={}", id);
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("Not found user with id=" + id));
    }

    @Override
    public List<User> getAll() {
        log.info("get all users");
        return new ArrayList<>(userRepo.findAll());
    }

    @Override
    public User update(User user) {
        log.info("update user {}", user);
        if (ValidationUtil.isNew(user))
            throw new NotFoundException("user doesn't exist");
        return userRepo.save(user);
    }
}

