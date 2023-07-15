package ru.net.serbis.mega.service.action;

import android.accounts.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.util.regex.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.task.*;
import android.text.*;

public class Action implements LoginCallback, FetchCallback, BrowserCallback, FileListCallback
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
		this.messenger = msg.replyTo;
		this.context = app.getApplicationContext();
		initEmailPath(msg);
	}
	
	protected void initEmailPath(Message msg)
	{
		String emailPath = getPath(msg);
		if (!TextUtils.isEmpty(emailPath))
		{
			Matcher matcher = Constants.PATH_PATTERN.matcher(emailPath);
			if (matcher.matches())
			{
				email = matcher.group(1);
				path = matcher.group(2);
			}
		}
	}

	protected String getPath(Message msg)
	{
		return msg.getData().getString(Constants.PATH);
	}
	
	public void execute()
	{
		if (!Utils.isNetworkAvailable(context))
		{
			sendError(Constants.ERROR_NETWORK_IS_NOT_AVAILABLE, context.getResources().getString(R.string.error_network_is_not_available));
			return;
		}
		AccountManager manager = AccountManager.get(context);
		megaApi = app.getUserMegaApi(email);
		if (megaApi != null)
		{
			onFetched();
		}
		else
		{
			megaApi = app.getMegaApi(email);
			String password = manager.getPassword(new AccountMega(email));
			new LoginTask(megaApi, this).execute(email, password);
		}
	}
	
	@Override
	public void onLogin(String user)
	{
		app.addUserSession(user, megaApi);
		new FetchTask(megaApi, this).execute();
	}

	@Override
	public void progress(int progress)
	{
        Bundle data = new Bundle();
        data.putInt(Constants.PROGRESS, progress);
		sendResult(data);
	}

	@Override
	public void onError(MegaError error)
	{
		sendError(error.getErrorCode(), error.getErrorString());
	}
	
	@Override
	public void onFetched()
	{
	}

	protected void sendResult(String key, String value)
	{
        Bundle data = new Bundle();
        data.putString(key, value);
        sendResult(data);
	}
	
	protected void sendError(int errorCode, String error)
	{
		Toast.makeText(context, errorCode + ": " + error, Toast.LENGTH_LONG).show();
		
		Bundle data = new Bundle();
		data.putInt(Constants.ERROR_CODE, errorCode);
        data.putString(Constants.ERROR, error);
		sendResult(data);
	}
	
	protected void sendResult(Bundle data)
	{
        Message msg = Message.obtain();
        msg.setData(data);
        try
        {
            messenger.send(msg);
        }
        catch (RemoteException e)
        {
            Log.error(this, e);
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

    @Override
    public void result(String data)
    {
    }
}
