package ru.dictionary.repo;

import ru.dictionary.entity.Word;

import java.util.List;

public interface WordRepo {
    Word save(Word word, Integer userId);
    void delete(Integer wordId, Integer userId);
    void delete(List<Integer> wordIds, Integer userId);
    Word update(Word word, Integer userId);
    Word getById(Integer wordId, Integer userId);
    List<Word> getAll(Integer userId);
    List<Word> getUnlearnedWords(Integer userId, Integer trainingId);
}
