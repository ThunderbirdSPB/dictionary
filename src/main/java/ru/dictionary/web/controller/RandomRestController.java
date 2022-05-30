package ru.dictionary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dictionary.repo.RandomRepo;

import java.util.List;

@RestController
@RequestMapping(RandomRestController.REST_URL)
public class RandomRestController {
    static final String REST_URL = "/random";
    private static final int NUM_OF_RANDOM = 10;
    private static final Logger log = LoggerFactory.getLogger(RandomRestController.class);

    private final RandomRepo repo;

    public RandomRestController(RandomRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/words")
    public List<String> getRandomWords(@RequestParam List<String> words){
        log.info("get random words");

        return repo.getRandomWords(words, NUM_OF_RANDOM);
    }

    @GetMapping("/translations")
    public List<String> getRandomTranslations(@RequestParam List<String> translations){
        log.info("get random translations");

        return repo.getRandomTranslations(translations, NUM_OF_RANDOM);
    }
}
