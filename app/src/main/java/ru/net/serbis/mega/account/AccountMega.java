package ru.net.serbis.mega.account;

import android.accounts.*;
import android.os.*;

public class AccountMega extends Account
{
	public static final String TYPE = "ru.net.serbis.mega";
	public static final String TOKEN = TYPE + ".TOKEN";
	
	public AccountMega(Parcel parcel)
	{
		super(parcel);
	}
	
	public AccountMega(String name)
	{
		super(name, TYPE);
	}
}
