package ru.net.serbis.mega.account;

import android.accounts.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.activity.*;

public class Authenticator extends AbstractAccountAuthenticator
{
	private App app;
	
	public Authenticator(Context context)
	{
		super(context);
		this.app = (App) context;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType)
	{
		return null;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String tokenType, String[] features, Bundle options) throws NetworkErrorException
	{
		Intent intent = new Intent(app.getApplicationContext(), Login.class);
		intent.putExtra(Constants.TOKEN_TYPE, accountType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException
	{
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String tokenType, Bundle options) throws NetworkErrorException
	{
		return null;
	}

	@Override
	public String getAuthTokenLabel(String tokenType)
	{
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String tokenType, Bundle options) throws NetworkErrorException
	{
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException
	{
		return null;
	}
}
