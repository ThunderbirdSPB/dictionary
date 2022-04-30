package ru.dictionary.repo.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dictionary.repo.AbstractRepoTest;
import ru.dictionary.repo.RandomRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCRandomRepoTest extends AbstractRepoTest {
    @Autowired
    RandomRepo repo;

    @Test
    void getRandomWords() {
        System.out.println(repo.getRandomWords(List.of("private", "public", "protected"), 40));
    }

    @Test
    void getRandomTranslations() {
        System.out.println(repo.getRandomTranslations(List.of("общедоступный", "частный", "защищенный"), 40));
    }
}