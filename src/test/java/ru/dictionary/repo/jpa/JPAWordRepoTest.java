package ru.dictionary.repo.jpa;



import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordRepoTest;
import ru.dictionary.util.exception.NotFoundException;


import javax.persistence.NoResultException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.ActiveDbProfileResolver.JPA;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.testData.WordTestData.APPLE_ID;

@ActiveProfiles(JPA)
class JPAWordRepoTest extends AbstractWordRepoTest {
    @Test
    @Override
    protected void getById_byNotExistingId_exception() {
        assertThrows(NoResultException.class, () -> repo.getById(NOT_EXISTING_ID, USER_ID));
    }

    @Test
    @Override
    protected void update_existingWordWithNotExistingUser_exception() {
        assertThrows(NoResultException.class, () -> repo.update(UPDATED_APPLE, NOT_EXISTING_ID));
    }

    @Test
    @Override
    protected void delete_byExistingWordIdsAndExistingUserId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, USER_ID));
        repo.delete(List.of(1,2,3), USER_ID);

        assertThrows(NoResultException.class, () -> repo.getById(APPLE_ID, USER_ID));
    }

    @Override
    protected void update_notExistingWord_exception() {
        assertThrows(NoResultException.class, () -> repo.update(NEW_WORD, USER_ID));
    }

    @Override
    protected void delete_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, USER_ID));
        repo.delete(APPLE_ID, USER_ID);

        assertThrows(NoResultException.class, () -> repo.getById(APPLE_ID, USER_ID));
    }
}