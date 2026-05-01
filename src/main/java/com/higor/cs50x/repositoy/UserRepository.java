package com.higor.cs50x.repositoy;

import com.higor.cs50x.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>
{
    Optional<User> findByEmail(String email);

    boolean existsByEmailOrCellphoneOrCpf(String email, String cellphone, String cpf);
}
