package com.higor.cs50x.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        String path,
        List<String> errors
)
{
}
