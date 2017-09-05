package ru.net.serbis.mega.service;
import android.accounts.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import java.util.regex.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.account.*;
import ru.net.serbis.mega.data.*;
import ru.net.serbis.mega.task.*;

public class FilesList implements LoginCallback, FetchCallback, LogoutCallback
{
	private static final Pattern PATH = Pattern.compile("^\\/\\/" + Constants.SBMEGA + "\\/(.*?)(\\/.*)$");
	private App app;
	private MegaApiAndroid megaApi;
	private Messenger messenger;
	private String email;
	private String path;
	private Context context;
	private String fileslist;

	public FilesList(App app, Message msg)
	{
		this.app = app;
		this.megaApi = app.getMegaApi();
		this.messenger = msg.replyTo;
		this.context = app.getApplicationContext();
		initEmailPath(msg);
	}

	private void initEmailPath(Message msg)
	{
		Matcher matcher = PATH.matcher(getPath(msg));
		if (matcher.matches())
		{
			email = matcher.group(1);
			path = matcher.group(2);
		}
	}

	private String getPath(Message msg)
	{
		return msg.getData().getString(Constants.PATH);
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
		Toast.makeText(context, error.getErrorCode() + ": " + error.getErrorString(), Toast.LENGTH_LONG).show();
	}	
	
	@Override
	public void onFetched(MegaRequestListenerInterface listener)
	{
		MegaNode node = megaApi.getNodeByPath(path);
		
		BufferedWriter writer = null;
		try
		{
			File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
			File outputFile = File.createTempFile("files_list_", ".txt", outputDir);
			fileslist = outputFile.getAbsolutePath();
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			collectChild(writer, node);
			writer.flush();
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(writer);
		}

		new LogoutTask(megaApi, this).execute();
	}
	
	private void collectChild(BufferedWriter writer, MegaNode node) throws Exception
	{
		for(MegaNode children : megaApi.getChildren(node))
		{
			if (children.isFolder())
			{
				collectChild(writer, children);
			}
			else if (children.isFile())
			{
				writer.append("//" + Constants.SBMEGA + "/" + email + megaApi.getNodePath(children));
				writer.newLine();
			}
		}
	}

	@Override
	public void onLogout()
	{
		send();
	}

	private void send()
	{
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(Constants.FILES_LIST, fileslist);
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

	public void execute()
	{
		AccountManager manager = AccountManager.get(context);
		String password = manager.getPassword(new AccountMega(email));
		new LoginTask(megaApi, this).execute(email, password);
	}
}
