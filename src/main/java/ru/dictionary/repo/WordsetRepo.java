package ru.dictionary.repo;

import ru.dictionary.entity.Word;
import ru.dictionary.entity.Wordset;

import java.util.List;

public interface WordsetRepo {
    Wordset save(Wordset wordset, Integer userId);
    void delete(Integer wordsetId, Integer userId);
    void delete(List<Integer> wordsetIds, Integer userId);
    Wordset rename(Integer wordsetId, Integer userId, String name);
    Wordset getById(Integer wordsetId, Integer userId);
    List<Wordset> getAll(Integer userId);

    List<Word> getWords(Integer wordsetId, Integer userId);
    void deleteWord(Integer wordId, Integer wordsetId, Integer userId);
    void deleteWords(List<Integer> wordIds, Integer wordsetId, Integer userId);
    void addWord(Integer wordId, Integer wordsetId, Integer userId);
    void addWords(List<Integer> wordIds, Integer wordsetId, Integer userId);
}
