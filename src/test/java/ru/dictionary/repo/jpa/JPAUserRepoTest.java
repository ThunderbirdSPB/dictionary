package ru.dictionary.repo.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.entity.User;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.UserRepo;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static ru.dictionary.testData.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class JPAUserRepoTest extends AbstractRepoTest {
    @Autowired
    UserRepo repo;

    @Test
    void save(){
        User saved = repo.save(newUser);
        USER_MATCHER.assertMatch(saved, repo.get(saved.getId()));
    }

    @Test
    void saveInvalidEmail(){
        assertThrows(ConstraintViolationException.class, ()-> repo.save(userWithInvalidEmail));
    }

    @Test
    void get(){
        user.setId(USER_ID);
        USER_MATCHER.assertMatch(user, repo.get(USER_ID));
    }

    @Test
    void getAll(){
        USER_MATCHER.assertMatch(repo.getAll(), allUsers);
    }


    @Test
    void update(){
        USER_MATCHER.assertMatch(repo.update(updatedUser), repo.get(USER_ID));
    }

    @Test
    void delete(){
        repo.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> repo.get(USER_ID));
    }
}






