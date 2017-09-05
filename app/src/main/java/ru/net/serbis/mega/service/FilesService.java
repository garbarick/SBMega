package ru.net.serbis.mega.service;

import android.app.*;
import android.content.*;
import android.os.*;
import ru.net.serbis.mega.*;

public class FilesService extends Service
{
    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case Constants.ACTION_GET_FILES_LIST:
                    getFlesList(msg);
                    break;
                    
                default:
                    super.handleMessage(msg);
            }
        }
    }
    
    private Messenger messenger;
    private App app;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        messenger = new Messenger(new IncomingHandler());
        app = (App) getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return messenger.getBinder();
    }
    
    public void getFlesList(Message msg)
    {
		FilesList filesList = new FilesList(app, msg);
		filesList.execute();
    }
}
