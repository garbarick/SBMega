package ru.net.serbis.mega.task;

import android.os.*;
import nz.mega.sdk.*;

public class LogoutTask extends AsyncTask<Void, Void, Void> implements MegaRequestListenerInterface
{
	private MegaApiAndroid megaApi;
	private LogoutCallback callback;

	public LogoutTask(MegaApiAndroid megaApi, LogoutCallback callback)
	{
		this.megaApi = megaApi;
		this.callback = callback;
	}

	@Override
	protected Void doInBackground(Void[] p1)
	{
		megaApi.logout(this);
		return null;
	}

	public void onRequestStart(MegaApiJava api, MegaRequest request)
    {
    }

    public void onRequestUpdate(MegaApiJava api, MegaRequest request)
    {
    }

    public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
    {
		if (error.getErrorCode() == MegaError.API_OK)
		{
        	switch (request.getType())
        	{
            	case MegaRequest.TYPE_LOGOUT:
					callback.onLogout();
					break;
			}
        }
		else
		{
			callback.onError(error);
		}
    }

    public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError error)
    {
    }
}
