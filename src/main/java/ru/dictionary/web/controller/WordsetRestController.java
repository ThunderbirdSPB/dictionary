package ru.dictionary.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dictionary.entity.Wordset;
import ru.dictionary.entity.to.WordTO;
import ru.dictionary.entity.to.WordsetTO;
import ru.dictionary.repo.WordsetRepo;
import ru.dictionary.util.to.WordTOUtil;
import ru.dictionary.util.to.WordsetTOUtil;

import java.util.List;

import static ru.dictionary.util.SecurityUtil.authUserId;

@RequestMapping(WordsetRestController.REST_URL)
@RestController
public class WordsetRestController {
    static final String REST_URL = "/ws";
    private static final Logger log = LoggerFactory.getLogger(WordsetRestController.class);

    private final WordsetRepo repo;

    public WordsetRestController(WordsetRepo repo) {
        this.repo = repo;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WordsetTO save(@RequestParam String name){
        log.info("save wordset with name {}", name);

        return WordsetTOUtil.getTOFromWs(repo.save(new Wordset(null, name), authUserId()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        log.info("delete ws with id={}", id);

        repo.delete(id, authUserId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rename(@PathVariable Integer id, @RequestParam String name){
        log.info("rename ws with id={} to new name={}", id, name);
        repo.rename(id, authUserId(), name);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WordsetTO get(@PathVariable Integer id){
        log.info("get ws by id={}", id);
        return WordsetTOUtil.getTOFromWs(repo.getById(id, authUserId()));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordsetTO> get(){
        log.info("get wordsets for user with id={}", authUserId());
        return WordsetTOUtil.getTOsFromWs(repo.getAll(authUserId()));
    }

    @PostMapping("/{wsId}/words/{wordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addWord(@PathVariable Integer wsId, @PathVariable Integer wordId){
        log.info("add wordId={} to wsId={}", wordId, wsId);

        repo.addWord(wordId, wsId, authUserId());
    }

    @PostMapping("/{wsId}/words")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addWords(@PathVariable Integer wsId, @RequestParam List<Integer> wordIds){
        log.info("add wordIds={} to wsId={}", wordIds, wsId);

        repo.addWords(wordIds, wsId, authUserId());
    }

    @DeleteMapping("/{wsId}/words/{wordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWord(@PathVariable Integer wsId, @PathVariable Integer wordId){
        log.info("delete wordId={} from wsId={}", wordId, wsId);

        repo.deleteWord(wordId, wsId, authUserId());
    }

    @DeleteMapping("/{wsId}/words")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWords(@PathVariable Integer wsId, @RequestParam List<Integer> wordIds){
        log.info("delete wordIds={} from wsId={}", wordIds, wsId);

        repo.deleteWords(wordIds, wsId, authUserId());
    }

    @GetMapping(path = "/{wsId}/words", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WordTO> getWords(@PathVariable Integer wsId){
        log.info("get words from wsId={}", wsId);

        return WordTOUtil.getTOsFromWords(repo.getWords(wsId, authUserId()));
    }
}




















