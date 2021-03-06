package ru.dictionary.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "words")

@NamedQueries({
        @NamedQuery(name = Word.DELETE_BY_WORD_ID_AND_USER_ID, query = "DELETE FROM Word w WHERE w.id=:wordId AND w.user.id=:userId"),
        @NamedQuery(name = Word.DELETE_BY_WORD_IDS_AND_USER_ID, query = "DELETE FROM Word w WHERE w.user.id=:userId AND w.id IN (:wordIds)"),
        @NamedQuery(name = Word.GET_ALL_BY_USER_ID, query = "SELECT w FROM Word w WHERE w.user.id=:userId"),
        @NamedQuery(name = Word.GET_BY_ID_AND_USER_ID, query = "SELECT w FROM Word w Where w.id=:wordId AND w.user.id=:userId")
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = Word.GET_UNLEARNED_WORDS,
                query = "SELECT * FROM words WHERE id IN (SELECT word_id FROM user_word_training WHERE user_id=:userId AND training_id=:trainingId)",
                resultClass = Word.class
        ),
})
public class Word extends AbstractBaseEntity{
    public static final String DELETE_BY_WORD_ID_AND_USER_ID = "Word.deleteByWordIdAndUserId";
    public static final String DELETE_BY_WORD_IDS_AND_USER_ID = "Word.deleteByWordIdsAndUserId";

    public static final String GET_ALL_BY_USER_ID = "Word.getAllByUserId";
    public static final String GET_UNLEARNED_WORDS = "Word.getUnlearnedWords";
    public static final String GET_BY_ID_AND_USER_ID = "Word.getByIdAndUserId";

    @Column(name = "word", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    private String word;

    @Column(name = "translation", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    private String translation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Word(Integer id, String word, String translation) {
        super(id);
        this.word = word;
        this.translation = translation;
    }

    public Word(Integer id, String word, String translation, User user) {
        super(id);
        this.word = word;
        this.translation = translation;
        this.user = user;
    }

    public Word() {}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}


