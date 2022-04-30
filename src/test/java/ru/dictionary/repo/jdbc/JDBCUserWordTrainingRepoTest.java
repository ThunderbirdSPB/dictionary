package ru.dictionary.repo.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.UserWordTrainingRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCUserWordTrainingRepoTest extends AbstractRepoTest {
    @Autowired
    private UserWordTrainingRepo repo;

    @Test
    void moveWordToLearning() {
        repo.moveWordToLearning(2,4,1);
    }

    @Test
    void moveWordsToLearning() {
        repo.moveWordsToLearning(2, List.of(4,5,6), 1);
    }

    @Test
    void moveWordToLearned() {
        repo.moveWordToLearned(1,1,1);
    }

    @Test
    void moveWordsToLearned() {
        repo.moveWordsToLearned(1, List.of(1,2,3), 1);
    }

    @Test
    void getNumOfUnlearnedWords() {
    }

    @Test
    void getUnlearnedWords() {
    }

    @Test
    void getRandomWords() {
    }

    @Test
    void getRandomTranslations() {
    }
}