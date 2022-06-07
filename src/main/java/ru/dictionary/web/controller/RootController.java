package ru.dictionary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        log.info("login");
        return "login";
    }
}
