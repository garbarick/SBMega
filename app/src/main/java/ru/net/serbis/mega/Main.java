package ru.net.serbis.mega;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.task.*;

public class Main extends Activity implements MegaRequestListenerInterface
{
    private MegaApiAndroid megaApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        hide(R.id.login_progress);
        show(R.id.login_form);

        MainApplication app = (MainApplication) getApplication();
		megaApi = app.getMegaApi();

        initLogon();
    }

    private <T extends View> T findView(int id)
    {
        return (T) findViewById(id);
    }

    private String getEditText(int id)
    {
        EditText text = findView(id);
        return text.getText().toString();
    }

    private void hide(int id)
    {
        View view = findView(id);
        view.setVisibility(View.GONE);
    }

    private void show(int id)
    {
        View view = findView(id);
        view.setVisibility(View.VISIBLE);
    }

    private void initLogon()
    {
        Button logon = findView(R.id.logon);
        logon.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    logon();
                }
            }
        );
    }

    private void logon()
    {
        View view = this.getCurrentFocus();
        if (view != null)
        {  
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }        
        String email = getEditText(R.id.login_email);
        String password = getEditText(R.id.login_password);
        hide(R.id.login_form);
        show(R.id.login_progress);

        ProgressBar bar = findView(R.id.login_progress);
        bar.setMax(100);

        new LogonTask(megaApi, this).execute(email, password);
    }

    public void onLogon(String privateKey, String publicKey)
    {
        String email = getEditText(R.id.login_email);
        megaApi.fastLogin(email, publicKey, privateKey, this);
    }

    public void onRequestStart(MegaApiJava api, MegaRequest request)
    {
        Log.info(this, "RequestStart: " + request.getRequestString());
    }

    public void onRequestUpdate(MegaApiJava api, MegaRequest request)
    {
        Log.info(this, "RequesUpdate: " + request.getRequestString());

        switch (request.getType())
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
        }
    }

    public void onRequestFinish(MegaApiJava api, MegaRequest request, MegaError e)
    {
        Log.info(this, "RequestFinish: " + request.getRequestString());

        switch (request.getType())
        {
            case MegaRequest.TYPE_LOGIN:
                if (e.getErrorCode() == MegaError.API_OK)
                {
                    setTitle(getEditText(R.id.login_email));
                    megaApi.fetchNodes(this);
                }
                else
                {
                    String errorMessage = e.getErrorString();
                    switch (e.getErrorCode())
                    {
                        case MegaError.API_ENOENT:
                        case MegaError.API_EARGS:
                            errorMessage = getString(R.string.error_incorrect_email_or_password);
                            break;
                    }
                    Toast.makeText(this, e.getErrorCode() + ": " + errorMessage, Toast.LENGTH_LONG).show();
                    hide(R.id.login_progress);
                    show(R.id.login_form);
                }
                break;

            case MegaRequest.TYPE_FETCH_NODES:
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
                break;
        }
    }

    public void onRequestTemporaryError(MegaApiJava api, MegaRequest request, MegaError e)
    {
        Log.info(this, "RequestTemporaryError:" + e.getErrorString());
    }
}
