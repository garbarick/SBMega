package ru.net.serbis.mega.task;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.data.*;

public class LoginTask extends AsyncTask<String, Void, Token> implements MegaRequestListenerInterface
{
    private MegaApiAndroid megaApi;
    private LoginCallback callback;
	private Token token;
	private String email;

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
		this.email = email;
        String privateKey = megaApi.getBase64PwKey(password);
        String publicKey = megaApi.getStringHash(privateKey, email);
        return new Token(publicKey, privateKey);
    }

    @Override
    protected void onPostExecute(Token token)
    {
        login(token);
    }
	
	public void login(Token token)
	{
		this.token = token;
		megaApi.fastLogin(email, token.getPublicKey(), token.getPrivateKey(), this);
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
					callback.onLogin(email);
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
