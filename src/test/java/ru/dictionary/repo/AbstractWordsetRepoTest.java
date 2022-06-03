package ru.dictionary.repo;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.dictionary.entity.Wordset;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.testData.WordsetTestData.*;

public abstract class AbstractWordsetRepoTest extends AbstractRepoTest {
    @Autowired
    protected WordsetRepo repo;

    @Test
    void save_newWs_success() {
        Wordset saved = repo.save(NEW_WS, USER_ID);
        WORDSET_MATCHER.assertMatch(saved, repo.getById(saved.getId(), USER_ID));
    }

    @Test
    void save_invalidWs_exception() {
        assertThrows(ConstraintViolationException.class, () -> repo.save(INVALID_WS, USER_ID));
    }

    @Test
    void save_alreadyExistingWs_exception() {
       assertThrows(DataIntegrityViolationException.class, () -> repo.save(WS, USER_ID));
    }

    @Test
    void save_wsWithExistingName_exception() {
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(()->repo.save(NEW_WS_WITH_EXISTING_NAME, USER_ID))
                .withRootCauseInstanceOf(PSQLException.class);
    }

    @Test
    protected void delete_existingWs_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, USER_ID));
        repo.delete(WS_ID, USER_ID);
        assertThrows(NotFoundException.class, ()-> repo.getById(WS_ID, USER_ID));
    }

    @Test
    protected void delete_notExistingWs_exception() {
        assertThrows(NotFoundException.class, ()-> repo.delete(NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    void update_existingWS_success() {
        WORDSET_MATCHER.assertMatch(UPDATED_WS, repo.rename(WS_ID, USER_ID, NEW_NAME));
    }

    @Test
    protected void update_notExistingWS_exception() {
        assertThrows(NotFoundException.class, () -> repo.rename(NOT_EXISTING_WS_ID, USER_ID, NEW_NAME));
    }

    @Test
    protected void update_ExistingWSWithEmptyName_exception() {
        assertThrows(ConstraintViolationException.class, () -> repo.rename(WS_ID, USER_ID, ""));
    }

    @Test
    void getById_byExistingId_success() {
        WORDSET_MATCHER.assertMatch(WS, repo.getById(WS_ID, USER_ID));
    }

    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getById(NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    void getAll() {
        WORDSET_MATCHER.assertMatch(ALL_WS, repo.getAll(USER_ID));
    }

    @Test
    void getWords_byExistingWsId_success() {
        WORD_MATCHER.assertMatch(ALL_WORDS_FOR_USER, repo.getWords(WS_ID, USER_ID));
    }

    @Test
    protected void getWords_byNotExistingWsId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getWords(NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    void deleteWord_byExistingWordIdAndExistingWorsetId_success() {
        repo.deleteWord(APPLE_ID, WS_ID, USER_ID);
    }

    @Test
    void deleteWord_byNotExistingWordIdAndExistingWordsetId_exception() {
        assertThrows(NotFoundException.class, ()-> repo.deleteWord(Integer.MAX_VALUE, WS_ID, USER_ID));
    }

    @Test
    void deleteWord_byExistingWordIdAndNotExistingWordsetId_exception() {
        assertThrows(NotFoundException.class, ()-> repo.deleteWord(Integer.MAX_VALUE, WS_ID, USER_ID));
    }

    @Test
    void deleteWords_byExistingWordset_success() {
        repo.deleteWords(ALL_USERS_WORD_IDS, WS_ID, USER_ID);
    }

    @Test
    protected void deleteWords_byNotExistingWordset_exception() {
        assertThrows(NotFoundException.class, () -> repo.deleteWords(ALL_USERS_WORD_IDS, NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    void addWord_addNotAddedWordToExistingWordset_success() {
        repo.addWord(NOT_ADDED_WORD_IDS.get(0), WS_ID, USER_ID);
    }

    @Test
    protected void addWord_addAddedWordToExistingWordset_exception() {
        assertThatExceptionOfType(Exception.class)
                .isThrownBy(()-> repo.addWord(ALL_USERS_WORD_IDS.get(0), WS_ID, USER_ID))
                .withRootCauseInstanceOf(PSQLException.class);
    }

    @Test
    protected void addWord_addNotAddedWordToNotExistingWordset_exception() {
        assertThrows(NotFoundException.class, ()-> repo.addWord(NOT_ADDED_WORD_IDS.get(0), NOT_EXISTING_WS_ID, USER_ID));
    }

    @Test
    void addWords_addNotAddedWordsToExistingWordset_success() {
        repo.addWords(NOT_ADDED_WORD_IDS, WS_ID, USER_ID);
    }

    @Test
    void addWords_addAddedWordsToExistingWordset_exception() {
        assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> repo.addWords(ALL_USERS_WORD_IDS, WS_ID, USER_ID));
    }
}
