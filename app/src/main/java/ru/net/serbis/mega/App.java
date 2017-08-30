package ru.net.serbis.mega;

import android.app.*;
import android.content.pm.*;
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
			String path = null;
			try
			{
				PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
				path = info.applicationInfo.dataDir + "/";
			}
			catch (Throwable e)
			{
				Log.info(this, e);
			}
			
			megaApi = new MegaApiAndroid(App.APP_KEY, App.USER_AGENT, path);
		}
		return megaApi;
	}
}
