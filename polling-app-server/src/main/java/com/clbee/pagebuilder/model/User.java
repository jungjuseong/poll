package com.clbee.pagebuilder.model;

import org.hibernate.annotations.NaturalId;

import com.clbee.pagebuilder.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "poll_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "user_id"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
@JsonIgnoreProperties(
        value = {"last_name", "first_name"},
        allowGetters = true
)
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_seq")
    private Long id;

    @Size(max = 20)
    @Column(name ="last_name")
    private String lastname;

    @Size(max = 20)
    @Column(name ="first_name")
    private String firstname;
    
    
    @NotBlank
    @Size(max = 15)
    @Column(name ="user_id")
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name ="user_pw")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "poll_user_roles",
            joinColumns = @JoinColumn(name = "user_seq"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String lastname, String firstname, String username, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;

        this.username = username;
        this.email = email;
        this.password = password;
    }


}