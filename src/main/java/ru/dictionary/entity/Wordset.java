package ru.dictionary.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "wordsets")
@NamedQueries({
        @NamedQuery(name = Wordset.DELETE_BY_WS_ID_AND_USER_ID, query = "DELETE FROM Wordset w WHERE w.id = :wordsetId AND w.user.id = :userId"),
        @NamedQuery(name = Wordset.DELETE_BY_WS_IDS_AND_USER_ID, query = "DELETE FROM Wordset w WHERE w.user.id = :userId AND w.id IN (:wordsetIds)"),
        @NamedQuery(name = Wordset.GET_ALL_BY_USER_ID, query = "SELECT w FROM Wordset w WHERE w.user.id = :userId"),
        @NamedQuery(name = Wordset.GET_WORDS_BY_WS_ID_AND_USER_ID, query = "SELECT ws FROM Wordset ws INNER JOIN FETCH ws.words WHERE ws.user.id = :userId AND ws.id = :wordsetId"),
        @NamedQuery(name = Wordset.GET_BY_WS_ID_AND_USER_ID, query = "SELECT ws FROM Wordset ws WHERE ws.id=:wsId AND ws.user.id=:userId")
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = Wordset.DELETE_WORD_BY_WORDSET_ID_AND_WORD_ID,
                query = "DELETE FROM wordset_word WHERE word_id = :wordId AND wordset_id = :wordsetId"
        ),
        @NamedNativeQuery(
                name = Wordset.DELETE_WORDS_BY_WORDSET_ID_AND_WORD_IDS,
                query = "DELETE FROM wordset_word WHERE wordset_id = :wordsetId AND word_id IN (:wordIds)"
        ),
        @NamedNativeQuery(
                name = Wordset.ADD_WORD_TO_WS,
                query = "INSERT INTO wordset_word VALUES (:wordId, :wordsetId)"
        )
})
public class Wordset extends AbstractBaseEntity{
    public static final String DELETE_BY_WS_ID_AND_USER_ID = "Wordset.deleteByWsIdAndUserId";
    public static final String DELETE_BY_WS_IDS_AND_USER_ID = "Wordset.deleteByWsIdsAndUserId";
    public static final String GET_ALL_BY_USER_ID = "Wordset.getAllByUserId";
    public static final String GET_WORDS_BY_WS_ID_AND_USER_ID = "Wordset.getWords";
    public static final String GET_BY_WS_ID_AND_USER_ID = "Wordset.getByWsIdAndUserId";
    public static final String INSERT_WORDS = "INSERT INTO wordset_word (word_id, wordset_id) VALUES (?, ?)";
    public static final String DELETE_WORD_BY_WORDSET_ID_AND_WORD_ID = "Word.deleteWordByWordIdAndWordsetId";
    public static final String DELETE_WORDS_BY_WORDSET_ID_AND_WORD_IDS = "Word.deleteWordsByWordIdsAndWordsetId";

    public static final String ADD_WORD_TO_WS = "Word.addWordToWordset";

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wordset_word",
            joinColumns = @JoinColumn(name = "wordset_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id")
    )
    private List<Word> words;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Wordset(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Wordset() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}





