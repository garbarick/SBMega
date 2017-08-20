package ru.net.serbis.mega.task;

import android.os.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.data.*;
import nz.mega.sdk.*;

public class LogonTask extends AsyncTask<String, Void, TwoStrings>
{
    private MegaApiAndroid megaApi;
    private Main main;

    public LogonTask(MegaApiAndroid megaApi, Main main)
    {
        this.megaApi = megaApi;
        this.main = main;
    }
    
    @Override
    protected TwoStrings doInBackground(String... params)
    {
        String privateKey = megaApi.getBase64PwKey(params[1]);
        String publicKey = megaApi.getStringHash(privateKey, params[0]);
        return new TwoStrings(privateKey, publicKey);
    }

    @Override
    protected void onPostExecute(TwoStrings result)
    {
        main.onLogon(result.getFirst(), result.getSecond());
    }
}
