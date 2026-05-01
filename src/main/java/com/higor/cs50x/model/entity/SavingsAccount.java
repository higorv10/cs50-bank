package com.higor.cs50x.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SAVINGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavingsAccount extends Account
{
    @Column(precision = 5, scale = 4, nullable = false)
    private BigDecimal interestRate;
}
