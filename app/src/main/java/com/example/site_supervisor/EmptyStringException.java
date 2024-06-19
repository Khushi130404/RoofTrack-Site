package com.example.site_supervisor;

public class EmptyStringException extends Exception
{
    private String errCode;

    EmptyStringException()
    {
        errCode = "Please provide a value..!";
    }
    @Override
    public String toString()
    {
        return errCode;
    }
}
