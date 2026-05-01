package com.higor.cs50x.dto.response;

import com.higor.cs50x.model.entity.User;

public record RegisterUserResponse(
        String message,
        String name,
        String email
)
{
    public RegisterUserResponse(User user)
    {
        this(
                "User successfully registered",
                user.getName(),
                user.getEmail()
        );
    }
}
