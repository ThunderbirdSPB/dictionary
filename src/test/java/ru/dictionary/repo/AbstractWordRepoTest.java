package ru.dictionary.repo;

import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.entity.Word;
import ru.dictionary.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.dictionary.testData.WordTestData.*;
import static ru.dictionary.util.SecurityUtil.authUserId;

public abstract class AbstractWordRepoTest extends AbstractRepoTest{
    @Autowired
    protected WordRepo repo;

    @Test
    void save_newWord_success() {
        Word saved = repo.save(NEW_WORD, authUserId());
        WORD_MATCHER.assertMatch(saved, repo.getById(saved.getId(), authUserId()));
    }

    @Test
    void save_emptyWord_exception() {
        assertThrows(ConstraintViolationException.class, () -> repo.save(EMPTY_WORD, authUserId()));
    }

    @Test
    protected void delete_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
        repo.delete(APPLE_ID, authUserId());

        assertThrows(NotFoundException.class, () -> repo.getById(APPLE_ID, authUserId()));
    }

    @Test
    protected void delete_byExistingWordIdsAndExistingUserId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
        repo.delete(List.of(1,2,3), authUserId());

        assertThrows(NotFoundException.class, () -> repo.getById(APPLE_ID, authUserId()));
    }

    @Test
    void update_existingWord_success() {
        WORD_MATCHER.assertMatch(UPDATED_APPLE, repo.update(UPDATED_APPLE, authUserId()));
    }

    @Test
    void update_invalidWord_exception() {
        assertThrows(ConstraintViolationException.class, () -> repo.update(EMPTY_WORD, authUserId()));
    }

    @Test
    void update_notExistingWord_exception() {
        assertThrows(NotFoundException.class, () -> repo.update(EMPTY_WORD, authUserId()));
    }

    @Test
    void getById_byExistingId_success() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
    }

    @Test
    protected void getById_byNotExistingId_exception() {
        assertThrows(NotFoundException.class, () -> repo.getById(NOT_EXISTING_ID, authUserId()));
    }

    @Test
    void getAll() {
        WORD_MATCHER.assertMatch(ALL_WORDS_FOR_USER, repo.getAll(authUserId()));
    }

    @Test
    void getUnlearnedWords(){
        WORD_MATCHER.assertMatch(UNLEARNED_WORDS_FOR_USER, repo.getUnlearnedWords(authUserId(), 1));
    }
}
