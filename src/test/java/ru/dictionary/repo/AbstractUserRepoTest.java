package ru.dictionary.repo;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import ru.dictionary.entity.User;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.testData.UserTestData.*;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.WordTestData.EMPTY_WORD;

public abstract class AbstractUserRepoTest extends AbstractRepoTest {
    @Autowired
    private UserRepo repo;

    @Test
    void save_newUser_success(){
        User saved = repo.save(newUser);
        USER_MATCHER.assertMatch(saved, repo.get(saved.getId()));
    }

    @Test
    void save_userWithInvalidEmail_exception(){
        assertThrows(ConstraintViolationException.class, ()-> repo.save(userWithInvalidEmail));
    }

    @Test
    void save_alreadyExistingEmail_exception(){
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(()->repo.save(user))
                .withRootCauseInstanceOf(PSQLException.class);
    }

    @Test
    void get_byUserId_success(){
        user.setId(USER_ID);
        USER_MATCHER.assertMatch(user, repo.get(USER_ID));
    }

    @Test
    void getAll(){
        USER_MATCHER.assertMatch(repo.getAll(), allUsers);
    }


    @Test
    void update_ExistingUser_success(){
        USER_MATCHER.assertMatch(repo.update(updatedUser), repo.get(USER_ID));
    }

    @Test
    void update_NotExistingUser_exception(){
        assertThrows(NotFoundException.class, () -> repo.update(user));
    }

    @Test
    void delete_ExistingUser_success(){
        repo.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> repo.get(USER_ID));
    }

    @Test
    protected void delete_NotExistingUser_exception(){
        assertThrows(NotFoundException.class, () -> repo.delete(NOT_EXISTING_USER_ID));
    }
}
