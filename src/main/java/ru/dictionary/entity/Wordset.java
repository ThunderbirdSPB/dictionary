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
        @NamedQuery(name = Wordset.DELETE_BY_ID, query = "DELETE FROM Wordset w WHERE w.id = :wordsetId AND w.user.id = :userId"),
        @NamedQuery(name = Wordset.DELETE_BY_IDS, query = "DELETE FROM Wordset w WHERE w.user.id = :userId AND w.id IN (:wordsetIds)"),
        @NamedQuery(name = Wordset.GET_ALL, query = "SELECT w FROM Wordset w WHERE w.user.id = :userId"),
        @NamedQuery(name = Wordset.GET_WORDS, query = "SELECT ws FROM Wordset ws INNER JOIN FETCH ws.words WHERE ws.user.id = :userId AND ws.id = :wordsetId"),
})
public class Wordset extends AbstractBaseEntity{
    public static final String DELETE_BY_ID = "Wordset.deleteById";
    public static final String DELETE_BY_IDS = "Wordset.deleteByIds";
    public static final String GET_ALL = "Wordset.getAll";
    public static final String GET_WORDS = "Wordset.getWords";

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





