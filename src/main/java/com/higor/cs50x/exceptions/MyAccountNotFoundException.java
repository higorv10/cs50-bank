package com.higor.cs50x.exceptions;

public class MyAccountNotFoundException extends RuntimeException
{
    public MyAccountNotFoundException(String message)
    {
        super(message);
    }
}
