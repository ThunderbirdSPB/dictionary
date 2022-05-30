package ru.dictionary.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import ru.dictionary.entity.Word;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.WordTestData.*;

public abstract class AbstractWordRepoTest extends AbstractRepoTest{
    @Autowired
    protected WordRepo repo;

    @Test
    void save_newWord_success() {
        Word saved = repo.save(NEW_WORD, USER_ID);
        WORD_MATCHER.assertMatch(saved, repo.getById(saved.getId(), USER_ID));
    }

    @Test
    void save_emptyWord_exception() {
        assertThrows(ConstraintViolationException.class, () -> repo.save(INVALID_WORD, USER_ID));
    }

    @Test
    protected void delete_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, USER_ID));
        repo.delete(APPLE_ID, USER_ID);

        assertThrows(NotFoundException.class, () -> repo.getById(APPLE_ID, USER_ID));
    }

    @Test
    protected void delete_byExistingIdAndNotExistingUserId_exception() {
        assertThrows(NotFoundException.class, () -> repo.delete(APPLE_ID, NOT_EXISTING_ID));
    }

    @Test
    protected void delete_byNotExistingIdAndExistingUserId_exception() {
        assertThrows(NotFoundException.class, () -> repo.delete(NOT_EXISTING_ID, USER_ID));
    }

    @Test
    protected void delete_byExistingWordIdsAndExistingUserId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, USER_ID));
        repo.delete(List.of(1,2,3), USER_ID);

        assertThrows(NotFoundException.class, () -> repo.getById(APPLE_ID, USER_ID));
    }

    @Test
    void update_existingWord_success() {
        WORD_MATCHER.assertMatch(UPDATED_APPLE, repo.update(UPDATED_APPLE, USER_ID));
    }

    @Test
    protected void update_existingWordWithNotExistingUser_exception() {
        assertThrows(NotFoundException.class, () -> repo.update(UPDATED_APPLE, NOT_EXISTING_ID));
    }

    @Test
    void update_invalidWord_exception() {
        assertThatExceptionOfType(TransactionSystemException.class)
                .isThrownBy(()->repo.update(EMPTY_WORD, USER_ID))
                .withRootCauseInstanceOf(ConstraintViolationException.class);
    }

    @Test
    protected void update_notExistingWord_exception() {
        assertThrows(NotFoundException.class, () -> repo.update(NEW_WORD, USER_ID));
    }

    @Test
    void getById_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, USER_ID));
    }

    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getById(NOT_EXISTING_ID, USER_ID));
    }

    @Test
    void getAll() {
        WORD_MATCHER.assertMatch(ALL_WORDS_FOR_USER, repo.getAll(USER_ID));
    }

    @Test
    void getUnlearnedWords(){
        WORD_MATCHER.assertMatch(UNLEARNED_WORDS_FOR_USER, repo.getUnlearnedWords(USER_ID, 1));
    }
}
