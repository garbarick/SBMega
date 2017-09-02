package ru.net.serbis.mega.account;

import android.accounts.*;
import android.os.*;
import ru.net.serbis.mega.*;

public class AccountMega extends Account
{
	public AccountMega(Parcel parcel)
	{
		super(parcel);
	}
	
	public AccountMega(String name)
	{
		super(name, Constants.TYPE);
	}
}
