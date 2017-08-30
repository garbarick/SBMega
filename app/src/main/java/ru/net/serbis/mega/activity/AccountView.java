package ru.net.serbis.mega.activity;
import android.app.*;
import android.accounts.*;

public class AccountView
{
	Account account;

	public AccountView(Account account)
	{
		this.account = account;
	}

	public Account getAccount()
	{
		return account;
	}

	@Override
	public String toString()
	{
		return account.name;
	}
}
