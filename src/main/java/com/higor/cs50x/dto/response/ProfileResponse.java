package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.User;

public record ProfileResponse(
         long id,
         String name,
         String cpf,
         String cellphone,
         String email
)
{
    public ProfileResponse(User user)
    {
        this(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getCellphone(),
                user.getEmail()
        );
    }
}
