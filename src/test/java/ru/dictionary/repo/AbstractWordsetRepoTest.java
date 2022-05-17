package ru.dictionary.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import ru.dictionary.entity.Wordset;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.testData.WordsetTestData.*;
import static ru.dictionary.util.SecurityUtil.authUserId;

public abstract class AbstractWordsetRepoTest extends AbstractRepoTest {
    @Autowired
    protected WordsetRepo repo;

    @Test
    void save_newWs_success() {
        Wordset saved = repo.save(NEW_WS, authUserId());
        WORDSET_MATCHER.assertMatch(saved, repo.getById(saved.getId(), authUserId()));
    }

    @Test
    void save_invalidWs_success() {
        assertThrows(ConstraintViolationException.class, () -> repo.save(INVALID_WS, authUserId()));
    }

    @Test
    protected void delete_existingWs_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, authUserId()));
        repo.delete(WS_ID, authUserId());
        assertThrows(NotFoundException.class, ()-> repo.getById(WS_ID, authUserId()));
    }

    @Test
    void update_existingWS_success() {
        WORDSET_MATCHER.assertMatch(UPDATED_WS, repo.rename(WS_ID, authUserId(), NEW_NAME));
    }

    @Test
    protected void update_notExistingWS_exception() {
        assertThrows(NotFoundException.class, () -> repo.rename(NOT_EXISTING_WS_ID, authUserId(), NEW_NAME));
    }

    @Test
    protected void update_ExistingWSWithEmptyName_exception() {
        assertThrows(TransactionSystemException.class, () -> repo.rename(WS_ID, authUserId(), ""));
    }

    @Test
    void getById_byExistingId_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, authUserId()));
    }

    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getById(NOT_EXISTING_WS_ID, authUserId()));
    }

    @Test
    void getAll() {
        WORDSET_MATCHER.assertMatch(ALL_WS, repo.getAll(authUserId()));
    }

    @Test
    void getWords_byExistingWsId_success() {
        WORD_MATCHER.assertMatch(ALL_WORDS_FOR_USER, repo.getWords(WS_ID, authUserId()));
    }

    @Test
    protected void getWords_byNotExistingWsId_exception() {
        assertThrows(NoResultException.class, () -> repo.getWords(NOT_EXISTING_WS_ID, authUserId()));
    }

    @Test
    void deleteWord_byExistingWordIdAndExistingWorsetId_success() {
        repo.deleteWord(APPLE_ID, WS_ID, authUserId());
    }

    @Test
    void deleteWord_byNotExistingWordIdAndExistingWordsetId_exception() {
        assertThrows(NotFoundException.class, ()-> repo.deleteWord(Integer.MAX_VALUE, WS_ID, authUserId()));
    }

    @Test
    void deleteWord_byExistingWordIdAndNotExistingWordsetId_exception() {
        assertThrows(NotFoundException.class, ()-> repo.deleteWord(Integer.MAX_VALUE, WS_ID, authUserId()));
    }

    @Test
    void deleteWords_byExistingWordset_success() {
        repo.deleteWords(WORD_IDS, WS_ID, authUserId());
    }

    @Test
    void deleteWords_byNotExistingWordset_exception() {
        assertThrows(EntityNotFoundException.class, () -> repo.deleteWords(WORD_IDS, NOT_EXISTING_WS_ID, authUserId()));
    }

    @Test
    void addWord_addNotAddedWordToExistingWordset_success() {
        repo.addWord(NOT_ADDED_WORD_IDS.get(0), WS_ID, authUserId());
    }

    @Test
    protected void addWord_addAddedWordToExistingWordset_exception() {
        assertThrows(PersistenceException.class, () -> repo.addWord(WORD_IDS.get(0), WS_ID, authUserId()));
    }

    @Test
    void addWord_addNotAddedWordToNotExistingWordset_exception() {
        assertThrows(EntityNotFoundException.class, ()-> repo.addWord(NOT_ADDED_WORD_IDS.get(0), NOT_EXISTING_WS_ID, authUserId()));
    }

    @Test
    void addWords_addNotAddedWordsToExistingWordset_success() {
        repo.addWords(NOT_ADDED_WORD_IDS, WS_ID, authUserId());
    }

    @Test
    void addWords_addAddedWordsToExistingWordset_exception() {
        assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> repo.addWords(WORD_IDS, WS_ID, authUserId()));
    }
}
