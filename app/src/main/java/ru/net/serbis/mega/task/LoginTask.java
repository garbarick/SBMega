package ru.net.serbis.mega.task;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.data.*;

public class LoginTask extends AsyncTask<String, Void, Token> implements MegaRequestListenerInterface
{
    private MegaApiAndroid megaApi;
    private LoginCallback callback;
	private Token token;

	public LoginTask(MegaApiAndroid megaApi)
    {
        this.megaApi = megaApi;
    }
	
    public LoginTask(MegaApiAndroid megaApi, LoginCallback callback)
    {
        this(megaApi);
        this.callback = callback;
    }
    
    @Override
    public Token doInBackground(String... params)
    {
		return getToken(params[0], params[1]);
    }
	
	public Token getToken(String email, String password)
    {
        return new Token(email, password);
    }

    @Override
    protected void onPostExecute(Token token)
    {
        login(token);
    }
	
	public void login(Token token)
	{
		this.token = token;
        megaApi.login(token.getEmail(), token.getPassword(), this);
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
            	case MegaRequest.TYPE_LOGIN:
					callback.onLogin(token.getEmail());
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
