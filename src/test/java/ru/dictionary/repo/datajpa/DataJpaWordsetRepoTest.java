package ru.dictionary.repo.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.dictionary.repo.AbstractWordsetRepoTest;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.ActiveDbProfileResolver.DATAJPA;
import static ru.dictionary.testData.WordsetTestData.*;
import static ru.dictionary.testData.WordsetTestData.WS_ID;
import static ru.dictionary.util.SecurityUtil.authUserId;

@ActiveProfiles(DATAJPA)
public class DataJpaWordsetRepoTest extends AbstractWordsetRepoTest {
    @Override
    protected void delete_existingWs_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, authUserId()));
        repo.delete(WS_ID, authUserId());
        assertThrows(EntityNotFoundException.class, ()-> repo.getById(WS_ID, authUserId()));
    }

    @Override
    @Test
    protected void update_notExistingWS_exception() {
        assertThrows(EntityNotFoundException.class, () -> repo.rename(NOT_EXISTING_WS_ID, authUserId(), NEW_NAME));
    }

    @Override
    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(EntityNotFoundException.class, () -> repo.getById(NOT_EXISTING_WS_ID, authUserId()));
    }

    @Override
    @Test
    protected void getWords_byNotExistingWsId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getWords(NOT_EXISTING_WS_ID, authUserId()));
    }

    @Override
    @Test
    protected void addWord_addAddedWordToExistingWordset_exception() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.addWord(WORD_IDS.get(0), WS_ID, authUserId()));
    }
}
