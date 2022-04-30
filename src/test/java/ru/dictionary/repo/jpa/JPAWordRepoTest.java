package ru.dictionary.repo.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.entity.Word;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.WordRepo;
import ru.dictionary.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static ru.dictionary.util.SecurityUtil.authUserId;
import static ru.dictionary.testData.WordTestData.*;
import static org.junit.jupiter.api.Assertions.*;

class JPAWordRepoTest extends AbstractRepoTest {
    @Autowired
    WordRepo repo;

    @PersistenceContext
    EntityManager em;

    @Test
    void save() {
        Word saved = repo.save(NEW_WORD, authUserId());
        WORD_MATCHER.assertMatch(saved, repo.getById(saved.getId(), authUserId()));
    }

    @Test
    void delete() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
        repo.delete(APPLE_ID, authUserId());

        assertThrows(NotFoundException.class, () -> repo.getById(APPLE_ID, authUserId()));
    }

    @Test
    void update() {
        WORD_MATCHER.assertMatch(UPDATED_APPLE, repo.update(UPDATED_APPLE, authUserId()));
    }

    @Test
    void getById() {
        WORD_MATCHER.assertMatch(APPLE, repo.getById(APPLE_ID, authUserId()));
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