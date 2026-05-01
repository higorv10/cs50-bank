package com.higor.cs50x.repositoy;


import com.higor.cs50x.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Integer>
{
    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.user.email = :email AND TYPE(a) = :type")
    boolean existsByEmailAndType(@Param("email") String email, @Param("type") Class<? extends Account> type);
    boolean existsByNumber(String number);

    List<Account> findByUserEmail(String email);

    Optional<Account> findByBranchAndNumber (String branch, String number);
    Optional<Account> findByUserEmailAndBranchAndNumber(String email,  String branch, String number);
}
