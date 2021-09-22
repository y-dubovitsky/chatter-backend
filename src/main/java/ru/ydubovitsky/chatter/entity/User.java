package ru.ydubovitsky.chatter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import ru.ydubovitsky.chatter.entity.enums.ERole;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(length = 1000, nullable = false)
    private String password;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "text")
    private String bio;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name="user_id"))
    private Set<ERole> roles = new HashSet<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user", //TODO Возможно тут мапать на user_table
            orphanRemoval = true
    )
    private List<Post> posts = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime registerDate;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public User() {

    }

    @PrePersist //Вызывается ПЕРЕД записью в БД
    protected void onCreate() {
        this.registerDate = LocalDateTime.now();
    }
}
