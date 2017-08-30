package ru.net.serbis.mega.data;
import java.io.*;

public class Token implements Serializable
{
	private String publicKey;
	private String privateKey;

	public Token(String publicKey, String privateKey)
	{
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public String getPublicKey()
	{
		return publicKey;
	}

	public String getPrivateKey()
	{
		return privateKey;
	}
}
