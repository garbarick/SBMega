package ru.net.serbis.mega.task;

import android.os.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;
import ru.net.serbis.mega.activity.*;
import ru.net.serbis.mega.data.*;
import android.content.*;

public class LoginTask extends AsyncTask<String, Void, Token> implements MegaRequestListenerInterface
{
    private MegaApiAndroid megaApi;
    private Login login;
	private Token token;

	public LoginTask(MegaApiAndroid megaApi)
    {
        this.megaApi = megaApi;
    }
	
	public LoginTask(App app)
	{
		this(app.getMegaApi());
	}
	
    public LoginTask(MegaApiAndroid megaApi, Login login)
    {
        this(megaApi);
        this.login = login;
    }
    
    @Override
    public Token doInBackground(String... params)
    {
        String privateKey = megaApi.getBase64PwKey(params[1]);
        String publicKey = megaApi.getStringHash(privateKey, params[0]);
        return new Token(publicKey, privateKey);
    }

    @Override
    protected void onPostExecute(Token token)
    {
        this.token = token;
		megaApi.fastLogin(login.getEmail(), token.getPublicKey(), token.getPrivateKey(), this);
    }
	
	public void onRequestStart(MegaApiJava api, MegaRequest request)
    {
        Log.info(this, "RequestStart: " + request.getRequestString());
    }

    public void onRequestUpdate(MegaApiJava api, MegaRequest request)
    {
        Log.info(this, "RequesUpdate: " + request.getRequestString());

        /*switch (request.getType())
        {
            case MegaRequest.TYPE_FETCH_NODES:
                {
                    ProgressBar bar = findView(R.id.login_progress);
                    bar.setProgress(33);

                    if (request.getTotalBytes() > 0)
                    {
                        double progressValue = 100 * request.getTransferredBytes() / request.getTotalBytes();
                        if (progressValue > 100 || progressValue < 0)
                        {
                            progressValue = 100;
                        }
                        bar.setProgress((int)progressValue);               
                    }
                }
        }*/
    }

    public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError error)
    {
        Log.info(this, "RequestFinish: " + request.getRequestString());

        switch (request.getType())
        {
            case MegaRequest.TYPE_LOGIN:
			{
                if (error.getErrorCode() == MegaError.API_OK)
                {
					login.onLogin(token);
                    //setTitle(getEditText(R.id.login_email));
                    //megaApi.fetchNodes(this);
                }
                else
                {
					login.onLoginError(error);
                }
			}
            break;

            /*case MegaRequest.TYPE_FETCH_NODES:
                if (e.getErrorCode() == MegaError.API_OK)
                {
                    //Intent intent = new Intent(this, NavigationActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);
                    //finish();
                }
                else
                {
                    Toast.makeText(this, e.getErrorString(), Toast.LENGTH_LONG).show();
                    hide(R.id.login_progress);
                    show(R.id.login_form);
                }
                break;*/
        }
    }

    public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError e)
    {
        Log.info(this, "RequestTemporaryError:" + e.getErrorString());
    }
}
