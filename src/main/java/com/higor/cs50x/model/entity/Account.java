package com.higor.cs50x.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "accounts")
public abstract class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String branch;

    @Column(unique = true, nullable = false)
    private String number;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String password;

    public void setUser(User user) {
        this.user = user;

        if (user != null && !user.getAccounts().contains(this))
        {
            user.getAccounts().add(this);
        }
    }
}
