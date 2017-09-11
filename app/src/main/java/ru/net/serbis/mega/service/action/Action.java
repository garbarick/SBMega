package ru.net.serbis.mega.service.action;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.util.regex.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.data.*;
import ru.net.serbis.mega.task.*;

public class Action implements LoginCallback, FetchCallback, LogoutCallback, BrowserCallback
{
	protected App app;
	protected MegaApiAndroid megaApi;
	protected Messenger messenger;
	protected String email;
	protected String path;
	protected Context context;
	
	public Action(App app, Message msg)
	{
		this.app = app;
		this.megaApi = app.getMegaApi();
		this.messenger = msg.replyTo;
		this.context = app.getApplicationContext();
		initEmailPath(msg);
	}
	
	protected void initEmailPath(Message msg)
	{
		Matcher matcher = Constants.PATH_PATTERN.matcher(getPath(msg));
		if (matcher.matches())
		{
			email = matcher.group(1);
			path = matcher.group(2);
		}
	}

	protected String getPath(Message msg)
	{
		return msg.getData().getString(Constants.PATH);
	}
	
	public void execute()
	{
		AccountManager manager = AccountManager.get(context);
		String password = manager.getPassword(new AccountMega(email));
		new LoginTask(megaApi, this).execute(email, password);
	}
	
	@Override
	public void onLogin(Token token, MegaRequestListenerInterface listener)
	{
		new FetchTask(megaApi, this).execute();
	}

	@Override
	public void progress(int persent)
	{
	}

	@Override
	public void onError(MegaError error)
	{
		String message = error.getErrorCode() + ": " + error.getErrorString();
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		sendResult(Constants.ERROR, message);
	}	
	
	@Override
	public void onFetched(MegaRequestListenerInterface listener)
	{
		logout();
	}
	
	protected void logout()
	{
		new LogoutTask(megaApi, this).execute();
	}
	
	@Override
	public void onLogout()
	{
	}

	protected void sendResult(String key, Object value)
	{
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(key, String.valueOf(value));
        msg.setData(data);
        try
        {
            messenger.send(msg);
        }
        catch (RemoteException e)
        {
            Log.info(this, e);
        }
	}
	
	@Override
	public void onRequestStart()
	{
	}

	@Override
	public void onRequestFinish()
	{
	}

	@Override
	public void onDownloadFinish(MegaTransfer transfer)
	{
	}

	@Override
	public void onMoveFinish()
	{
	}
}
