package ru.dictionary.testData;

import ru.dictionary.MatcherFactory;
import ru.dictionary.entity.Word;

import java.util.List;

public class WordTestData {
    public static final Word NEW_WORD = new Word(null, "new", "новый");
    public static final Word APPLE = new Word(1, "apple", "яблоко");
    public static final Word UPDATED_APPLE = new Word(1, "not apple", "не яблоко", UserTestData.userWithId);
    public static final Word EMPTY_WORD = new Word(1, "", "не яблоко", UserTestData.userWithId);
    public static final Word INVALID_WORD = new Word(null, "", "не яблоко", UserTestData.userWithId);

    public static final List<Word> ALL_WORDS_FOR_USER = List.of(
            APPLE,
            new Word(2,  "orange", "апельсин"),
            new Word(3,  "pear", "груша")
    );

    public static final List<Word> UNLEARNED_WORDS_FOR_USER = List.of(
            APPLE,
            new Word(2,  "orange", "апельсин"),
            new Word(3,  "pear", "груша")
    );

    public static final List<Integer> WORD_IDS_NOT_IN_WS = List.of(4,5,6);
    public static final List<Integer> WORD_IDS_IN_WS = List.of(1,2,3);


    public static final Integer APPLE_ID = 1;
    public static final Integer NOT_EXISTING_ID = Integer.MAX_VALUE;

    public static final MatcherFactory.Matcher<Word> WORD_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Word.class, "user");
}
