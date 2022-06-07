package ru.dictionary.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dictionary.AuthorizedUser;
import ru.dictionary.entity.User;
import ru.dictionary.repo.UserRepo;
import ru.dictionary.util.exception.NotFoundException;

@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo repo;

    public UserDetailsServiceImpl(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.getByEmail(username.toLowerCase());
        if (user == null)
            throw new NotFoundException("user not found by email=" + username);

        return new AuthorizedUser(user);
    }
}
