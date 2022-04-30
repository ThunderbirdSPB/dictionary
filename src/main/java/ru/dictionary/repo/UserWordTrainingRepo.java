package ru.dictionary.repo;

import java.util.List;

public interface UserWordTrainingRepo {
    void moveWordToLearning(Integer userId, Integer wordId, Integer trainingId);
    void moveWordsToLearning(Integer userId, List<Integer> wordIds, Integer trainingId);
    void moveWordToLearned(Integer userId, Integer wordId, Integer trainingId);
    void moveWordsToLearned(Integer userId, List<Integer> wordIds, Integer trainingId);
    int getNumOfUnlearnedWords(Integer userId, Integer trainingId);
}
