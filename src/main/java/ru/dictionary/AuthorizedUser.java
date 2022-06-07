package ru.dictionary;

import ru.dictionary.entity.Role;
import ru.dictionary.entity.User;

import java.util.List;


public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private final User user;
    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), List.of(Role.USER));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
