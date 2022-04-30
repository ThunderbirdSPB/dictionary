package ru.dictionary.testData;

import ru.dictionary.MatcherFactory;
import ru.dictionary.entity.Word;

import java.util.List;

public class WordTestData {
    public static final Word NEW_WORD = new Word(null, "new", "новый");
    public static final Word APPLE = new Word(1, "apple", "яблоко");
    public static final Word UPDATED_APPLE = new Word(1, "not apple", "не яблоко", UserTestData.userWithId);

    public static final List<Word> ALL_WORDS_FOR_USER = List.of(APPLE, new Word(2,  "orange", "апельсин"),
            new Word(3,  "pear", "груша"));
    public static final List<Word> UNLEARNED_WORDS_FOR_USER = List.of(
            APPLE,
            new Word(2,  "orange", "апельсин"),
            new Word(3,  "pear", "груша"));


    public static final Integer APPLE_ID = 1;

    public static final MatcherFactory.Matcher<Word> WORD_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Word.class, "user");
}
