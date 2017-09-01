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

public class Login extends AccountAuthenticatorActivity implements LoginCallback
{
	public static final String TOKEN_TYPE = AccountMega.TYPE + ".TOKEN_TYPE";
	public static final String ACCOUNT = AccountMega.TYPE + ".ACCOUNT";

    private MegaApiAndroid megaApi;
	private boolean create;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
		
		App app = (App) getApplication();
		megaApi = app.getMegaApi();

		Intent intent = getIntent();
		Account account = intent.getParcelableExtra(ACCOUNT);
		if (account != null)
		{
			AccountManager manager = AccountManager.get(this);
			login(account.name, manager.getPassword(account));
		}
		else
		{
			create = true;
        	hide(R.id.login_progress);
        	show(R.id.login_form);
        	initLogon();
		}
    }

    private <T extends View> T findView(int id)
    {
        return (T) findViewById(id);
    }

    private String getEditText(int id)
    {
        EditText text = findView(id);
        return text.getText().toString();
    }

    private void hide(int id)
    {
        View view = findView(id);
        view.setVisibility(View.GONE);
    }

    private void show(int id)
    {
        View view = findView(id);
        view.setVisibility(View.VISIBLE);
    }

    private void initLogon()
    {
        Button login = findView(R.id.login);
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
        String email = getEditText(R.id.login_email);
        String password = getEditText(R.id.login_password);

		login(email, password);
    }

	private void login(String email, String password)
	{
		hide(R.id.login_form);
        show(R.id.login_progress);

        ProgressBar bar = findView(R.id.login_progress);
        bar.setMax(100);

        new LoginTask(megaApi, this).execute(email, password);
	}

	@Override
	public void onLogin(Token token, MegaRequestListenerInterface listener)
	{
		if (create)
		{
			megaApi.logout(listener);
		}
		else
		{
			megaApi.fetchNodes(listener);
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
		Toast.makeText(this, error.getErrorCode() + ": " + errorMessage, Toast.LENGTH_LONG).show();

		if (create)
		{
			hide(R.id.login_progress);
			show(R.id.login_form);
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
		ProgressBar bar = findView(R.id.login_progress);
		bar.setProgress(persent);
	}
	
	public void onFetchNode()
	{
		Intent intent = new Intent(this, Browser.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	public void onLogout(Token token)
	{
		String email = getEditText(R.id.login_email);
		String password = getEditText(R.id.login_password);

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
}
