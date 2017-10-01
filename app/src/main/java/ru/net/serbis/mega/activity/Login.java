package ru.net.serbis.mega.activity;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.data.*;
import ru.net.serbis.mega.task.*;

public class Login extends AccountAuthenticatorActivity implements LoginCallback, FetchCallback, LogoutCallback
{
	private App app;
    private MegaApiAndroid megaApi;
	private boolean create;
    private Params params;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

		app = (App) getApplication();

		params = new Params(getIntent());
        if (params.selectMode)
        {
            setResult(RESULT_CANCELED);
        }
		if (!Utils.isNetworkAvailable(this))
		{
			onError(Constants.ERROR_NETWORK_IS_NOT_AVAILABLE, getResources().getString(R.string.error_network_is_not_available));
		}
        if (params.account != null)
		{
			AccountManager manager = AccountManager.get(this);
			login(params.account.name, manager.getPassword(params.account));
		}
		else
		{
			create = true;
        	Tools.show(this, R.id.login_form);
        	initLogon();
		}
    }

    private void initLogon()
    {
        Button login = Tools.findView(this, R.id.login);
        login.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    login();
                }
            }
        );
    }

    private void login()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {  
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }        
        String email = Tools.getEditText(this, R.id.login_email);
        String password = Tools.getEditText(this, R.id.login_password);

		login(email, password);
    }

	private void login(String email, String password)
	{
		Tools.hide(this, R.id.login_form);
		megaApi = app.getUserMegaApi(email);
		if (megaApi != null)
		{
			onFetched();
		}
		else
		{
			megaApi = app.getMegaApi(email);
        	new LoginTask(megaApi, this).execute(email, password);
		}
	}

	@Override
	public void onLogin(String user)
	{
		if (create)
		{
			new LogoutTask(megaApi, this).execute();
		}
		else
		{
			app.addUserSession(user, megaApi);
			new FetchTask(megaApi, this).execute();
		}
	}

	@Override
	public void onError(MegaError error)
	{
		String errorMessage = error.getErrorString();
		switch (error.getErrorCode())
		{
			case MegaError.API_ENOENT:
			case MegaError.API_EARGS:
				errorMessage = getString(R.string.error_incorrect_email_or_password);
				break;
		}
		onError(error.getErrorCode(), errorMessage);
	}

	private void onError(int errorCode, String error)
	{
		if (error != null)
		{
			Toast.makeText(this, errorCode + ": " +  error, Toast.LENGTH_LONG).show();
		}

		if (create)
		{
			Tools.show(this, R.id.login_form);
		}
		else if (params.selectMode)
        {
            finish();
        }
        else
		{
			Intent intent = new Intent(this, Accounts.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void progress(int persent)
	{
		ProgressBar bar = Tools.findView(this, R.id.login_progress);
		bar.setProgress(persent);
	}

	@Override
	public void onFetched()
	{
        if (params.selectMode)
        {
            Intent intent = new Intent(this, Browser.class);
            params.setActionSelectPath(intent);
			params.setAccount(intent, params.account);
            startActivityForResult(intent, 0);
        }
        else
        {
            Intent intent = new Intent(this, Browser.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			params.setAccount(intent, params.account);
            startActivity(intent);
            finish();
        }
	}

	@Override
	public void onLogout()
	{
		String email = Tools.getEditText(this, R.id.login_email);
		app.clearUserSession(email);
		String password = Tools.getEditText(this, R.id.login_password);

		AccountManager manager = AccountManager.get(this);
		Bundle result = new Bundle();

		AccountMega account = new AccountMega(email);
		if (manager.addAccountExplicitly(account, password, new Bundle()))
		{
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
		}
		else
		{
			result.putString(AccountManager.KEY_ERROR_MESSAGE, getString(R.string.error_account_already_exists));
		}

		setAccountAuthenticatorResult(result);
		setResult(RESULT_OK);
		finish();
	}

    @Override
    public void onBackPressed()
    {
        if (!params.selectMode)
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		Params params = new Params(data);
		if (params.selectMode)
		{
        	if (RESULT_OK == resultCode)
			{
                Intent intent = new Intent(getIntent());
                params.selectPath(intent, params.selectPath);
                setResult(RESULT_OK, intent);
            }
			finish();
        }
    }
}
