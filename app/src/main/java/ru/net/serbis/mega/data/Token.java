package ru.net.serbis.mega.data;

import java.io.*;

public class Token implements Serializable
{
	private String email;
	private String password;

    public Token(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
}
