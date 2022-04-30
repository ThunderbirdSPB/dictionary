package ru.dictionary.testData;

import ru.dictionary.MatcherFactory;
import ru.dictionary.entity.User;

import java.util.List;

public class UserTestData {
    public static final User user = new User(null, "user@yandex.ru", "password");
    public static final User userWithId = new User(1, "user@yandex.ru", "password");
    public static final User userWithInvalidEmail = new User(null, "invalid", "password");
    public static final User newUser = new User(null, "newEmail@gmail.com", "password");
    public static final User updatedUser = new User(1, "newEmail@gmail.com", "password");
    public static final List<User> allUsers = List.of(new User(1, "user@yandex.ru", "password"),
            new User(2, "admin@gmail.com", "admin"));

    public static final Integer USER_ID = 1;
    public static final String NEW_EMAIL = "newEmail@gmail.com";

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "words");
}
