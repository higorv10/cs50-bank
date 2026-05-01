package com.higor.cs50x.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHECKING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckingAccount extends Account
{
    private BigDecimal overdraftLimit;
}
