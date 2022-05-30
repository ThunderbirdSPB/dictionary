package ru.dictionary.repo.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.UserWordTrainingRepo;

import static org.junit.jupiter.api.Assertions.*;
import static ru.dictionary.testData.UserTestData.USER_ID;
import static ru.dictionary.testData.UserTestData.USER_ID_2;
import static ru.dictionary.testData.WordTestData.*;

class JDBCUserWordTrainingRepoTest extends AbstractRepoTest {
    private static final Integer TRAINING_ID = 1;
    @Autowired
    private UserWordTrainingRepo repo;

    @Test
    void moveWordToLearning_withValidData_success() {
        int num = repo.getNumOfUnlearnedWords(USER_ID_2, TRAINING_ID);
        repo.moveWordToLearning(USER_ID_2,WORD_IDS_NOT_IN_WS.get(0), TRAINING_ID);
        assertEquals(num + 1, repo.getNumOfUnlearnedWords(USER_ID_2, TRAINING_ID));
    }

    @Test
    void moveWordToLearning_withNotExistingWordId_exception(){
        assertThrows(DataIntegrityViolationException.class, () -> repo.moveWordToLearning(USER_ID, NOT_EXISTING_ID, TRAINING_ID));
    }

    @Test
    void moveWordsToLearning() {
        int num = repo.getNumOfUnlearnedWords(USER_ID_2, TRAINING_ID);
        repo.moveWordsToLearning(USER_ID_2, WORD_IDS_NOT_IN_WS, TRAINING_ID);
        assertEquals(num + WORD_IDS_NOT_IN_WS.size(), repo.getNumOfUnlearnedWords(USER_ID_2, TRAINING_ID));
    }

    @Test
    void moveWordToLearned() {
        int num = repo.getNumOfUnlearnedWords(USER_ID, TRAINING_ID);
        repo.moveWordToLearned(USER_ID, APPLE_ID, TRAINING_ID);
        assertEquals(num -1, repo.getNumOfUnlearnedWords(USER_ID, TRAINING_ID));
    }

    @Test
    void moveWordsToLearned() {
        int num = repo.getNumOfUnlearnedWords(USER_ID, TRAINING_ID);
        repo.moveWordsToLearned(USER_ID, WORD_IDS_IN_WS, TRAINING_ID);
        assertEquals(num - WORD_IDS_IN_WS.size(), repo.getNumOfUnlearnedWords(USER_ID, TRAINING_ID));
    }
}