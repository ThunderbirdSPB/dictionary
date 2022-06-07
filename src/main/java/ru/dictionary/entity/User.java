package ru.dictionary.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id"),
        @NamedQuery(name = User.GET_ALL, query = "SELECT u FROM User u"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT u FROM User u WHERE u.email=:email"),
})
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity{
    public static final String DELETE = "User.deleteByUserId";
    public static final String GET_ALL = "User.getAll";
    public static final String BY_EMAIL = "User.getByEmail";

    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user") // cascade = CascadeType.ALL, orphanRemoval = true
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    private List<Word> words;

    public User(Integer id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mail='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


