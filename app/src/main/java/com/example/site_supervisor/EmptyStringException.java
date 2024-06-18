package com.example.site_supervisor;

public class EmptyStringException extends Exception
{
    private String errCode;

    EmptyStringException()
    {
        errCode = "Please enter some value..!";
    }
    @Override
    public String toString()
    {
        return errCode;
    }
}
