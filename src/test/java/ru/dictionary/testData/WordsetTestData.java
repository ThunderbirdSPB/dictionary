package ru.dictionary.testData;

import ru.dictionary.MatcherFactory;
import ru.dictionary.entity.AbstractBaseEntity;
import ru.dictionary.entity.Wordset;

import java.util.List;
import java.util.stream.Collectors;

import static ru.dictionary.testData.WordTestData.*;

public class WordsetTestData {
    public static final Wordset NEW_WS = new Wordset(null, "new");
    public static final Wordset UPDATED_WS = new Wordset(1, "updated");
    public static final Wordset WS = new Wordset(1, "fruits");
    public static final Wordset NEW_WS_WITH_EXISTING_NAME = new Wordset(null, "fruits");
    public static final Wordset INVALID_WS = new Wordset(null, "");

    public static final List<Wordset> ALL_WS = List.of(WS);
    public static final List<Integer> WORD_IDS = ALL_WORDS_FOR_USER.stream().map(AbstractBaseEntity::getId).collect(Collectors.toList());
    public static final List<Integer> NOT_ADDED_WORD_IDS = List.of(4,5,6);

    public static final Integer WS_ID = 1;
    public static final Integer NOT_EXISTING_WS_ID = Integer.MAX_VALUE;
    public static final String NEW_NAME = "updated";

    public static final MatcherFactory.Matcher<Wordset> WORDSET_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Wordset.class, "user", "words");
}
