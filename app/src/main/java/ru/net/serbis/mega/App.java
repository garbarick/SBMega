package ru.net.serbis.mega;

import android.app.*;
import nz.mega.sdk.*;

public class App extends Application
{
	private static final String APP_KEY = "CaEiA3qr";
	private static final String USER_AGENT = "SB MEGA";
	
	private MegaApiAndroid megaApi;
	
    public class AndroidLogger implements MegaLoggerInterface
	{
        public void log(String time, int loglevel, String source, String message)
		{
            Log.info(this, source + ": " + message); 
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
			megaApi = new MegaApiAndroid(App.APP_KEY, App.USER_AGENT, getFilesDir().getParentFile() + "/");
		}
		return megaApi;
	}
}
