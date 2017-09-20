package ru.net.serbis.mega;

import android.app.*;
import java.util.*;
import nz.mega.sdk.*;

public class App extends Application
{
	private static final String APP_KEY = "CaEiA3qr";
	private static final String USER_AGENT = "SB MEGA";
	
	private MegaApiAndroid megaApi;
	private Map<String, MegaApiAndroid> users = new HashMap<String, MegaApiAndroid>();
	
    public class AndroidLogger implements MegaLoggerInterface
	{
        public void log(String time, int loglevel, String source, String message)
		{
            //Log.info(this, source + ": " + message); 
        }
    }
    
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		MegaApiAndroid.addLoggerObject(new AndroidLogger());
		MegaApiAndroid.setLogLevel(MegaApiAndroid.LOG_LEVEL_MAX);
	}
	
	public MegaApiAndroid getMegaApi()
	{
		if(megaApi == null)
		{
			megaApi = new MegaApiAndroid(App.APP_KEY, App.USER_AGENT, getFilesDir() + "/");
		}
		return megaApi;
	}
	
	public MegaApiAndroid getMegaApi(String user)
	{
		if (users.containsKey(user))
		{
			return users.get(user);
		}
		return null;
	}
	
	public void addUserSession(String user, MegaApiAndroid megaApi)
	{
		this.megaApi = null;
		Log.info(this, "add user session for " + user);
		users.put(user, megaApi);
	}
	
	public void clearUserSession(String user)
	{
		Log.info(this, "clear user session for " + user);
		users.remove(user);
	}
}
