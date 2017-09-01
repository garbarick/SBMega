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
	
	public LoginTask(App app)
	{
		this(app.getMegaApi());
	}
	
    public LoginTask(MegaApiAndroid megaApi, LoginCallback callback)
    {
        this(megaApi);
        this.callback = callback;
    }
    
    @Override
    public Token doInBackground(String... params)
    {
		email = params[0];
        String privateKey = megaApi.getBase64PwKey(params[1]);
        String publicKey = megaApi.getStringHash(privateKey, email);
        return new Token(publicKey, privateKey);
    }

    @Override
    protected void onPostExecute(Token token)
    {
        this.token = token;
		megaApi.fastLogin(email, token.getPublicKey(), token.getPrivateKey(), this);
    }
	
	public void onRequestStart(MegaApiJava api, MegaRequest request)
    {
    }

    public void onRequestUpdate(MegaApiJava api, MegaRequest request)
    {
        switch (request.getType())
        {
            case MegaRequest.TYPE_FETCH_NODES:
                {
                    if (request.getTotalBytes() > 0)
                    {
                        double progressValue = 100 * request.getTransferredBytes() / request.getTotalBytes();
                        if (progressValue > 100 || progressValue < 0)
                        {
                            progressValue = 100;
                        }
						callback.progress((int) progressValue);         
                    }
                }
        }
    }

    public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
    {
        switch (request.getType())
        {
            case MegaRequest.TYPE_LOGIN:
			{
                if (error.getErrorCode() == MegaError.API_OK)
                {
					callback.onLogin(token, this);
                }
                else
                {
					callback.onError(error);
                }
			}
            break;

            case MegaRequest.TYPE_FETCH_NODES:
			{
                if (error.getErrorCode() == MegaError.API_OK)
                {
                    callback.onFetchNode();
                }
                else
                {
                    callback.onError(error);
                }
			}
            break;
			
			case MegaRequest.TYPE_LOGOUT:
			{
				if (error.getErrorCode() == MegaError.API_OK)
				{
					callback.onLogout(token);
				}
				else
				{
					callback.onError(error);
				}
			}
        }
    }

    public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError error)
    {
    }
}
