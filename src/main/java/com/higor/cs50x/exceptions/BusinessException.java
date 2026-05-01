package com.higor.cs50x.exceptions;

public class BusinessException extends RuntimeException
{
    public BusinessException(String message)
    {
        super(message);
    }
}

