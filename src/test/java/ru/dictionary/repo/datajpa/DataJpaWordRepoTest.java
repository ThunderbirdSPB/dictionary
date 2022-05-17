package ru.dictionary.repo.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordRepoTest;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.ActiveDbProfileResolver.DATAJPA;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.testData.WordTestData.APPLE_ID;
import static ru.dictionary.util.SecurityUtil.authUserId;
@ActiveProfiles(DATAJPA)
public class DataJpaWordRepoTest extends AbstractWordRepoTest {
    @Override
    @Test
    protected void delete_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
        repo.delete(APPLE_ID, authUserId());

        assertThrows(EntityNotFoundException.class, () -> repo.getById(APPLE_ID, authUserId()));
    }

    @Override
    @Test
    protected void delete_byExistingWordIdsAndExistingUserId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
        repo.delete(List.of(1,2,3), authUserId());

        assertThrows(EntityNotFoundException.class, () -> repo.getById(APPLE_ID, authUserId()));
    }

    @Override
    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(EntityNotFoundException.class, () -> repo.getById(NOT_EXISTING_ID, authUserId()));
    }
}
