package ru.dictionary.repo;

import java.util.List;

public interface RandomRepo {
    List<String> getRandomWords(List<String> exclude, int num);
    List<String> getRandomTranslations(List<String> exclude, int num);
}
