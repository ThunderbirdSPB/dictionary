package ru.dictionary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dictionary.entity.Word;
import ru.dictionary.entity.to.WordTO;
import ru.dictionary.repo.WordRepo;

import java.net.URI;
import java.util.List;

import static ru.dictionary.util.SecurityUtil.authUserId;
import static ru.dictionary.util.to.WordTOUtil.*;

@RestController
@RequestMapping(WordRestController.REST_URL)
public class WordRestController {
    static final String REST_URL = "/words";
    private static final Logger log = LoggerFactory.getLogger(WordRestController.class);

    private final WordRepo repo;

    public WordRestController(WordRepo repo) {
        this.repo = repo;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WordTO> save(@Validated @RequestBody WordTO wordTO){
        log.info("save wordTO {}", wordTO);
        Word saved = repo.save(createNewWordFromTO(wordTO), authUserId());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/create/{id}")
                .buildAndExpand(saved.getId()).toUri();

        return ResponseEntity
                .created(uriOfNewResource)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getToFromWord(saved));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated @RequestBody WordTO wordTO){
        log.info("update wordTO {}", wordTO);
        repo.update(updateWordFromTO(wordTO), authUserId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        log.info("delete word by id {}", id);

        repo.delete(id, authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam List<Integer> ids){
        log.info("delete words by ids {}", ids);

        repo.delete(ids, authUserId());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WordTO get(@PathVariable Integer id){
        log.info("get word by id {}", id);

        return getToFromWord(repo.getById(id, authUserId()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordTO> get(){
        log.info("get words");

        return getTOsFromWords(repo.getAll(authUserId()));
    }

    

}


