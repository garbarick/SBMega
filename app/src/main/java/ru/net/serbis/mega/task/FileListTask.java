package ru.net.serbis.mega.task;

import android.os.*;
import java.io.*;
import java.util.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;

public class FileListTask extends AsyncTask<String, Integer, String>
{
    private MegaApiAndroid megaApi;
    private FileListCallback callback;
    private String fileslist;
    private long current;
    private long total;
    private String path;
    private String email;
    
    public FileListTask(MegaApiAndroid megaApi, FileListCallback callback)
    {
        this.megaApi = megaApi;
        this.callback = callback;
	}
    
    @Override
    protected String doInBackground(String... params)
    {
        publishProgress(0);
        email = params[0];
        path = params[1];
        generateFilesList();
        return fileslist;
    }
    
    private void generateFilesList()
    {
        MegaNode node = megaApi.getNodeByPath(path);
        BufferedWriter writer = null;
        try
        {
            File outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File outputFile = File.createTempFile("files_list_", ".txt", outputDir);
            fileslist = outputFile.getAbsolutePath();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
            collectChildren(writer, node);
            writer.flush();
        }
        catch (Throwable e)
        {
            Log.error(this, e);
        }
        finally
        {
            Utils.close(writer);
            publishProgress(0);
        }
	}
    
    private void collectChildren(BufferedWriter writer, MegaNode node) throws Exception
    {
        List<MegaNode> children = megaApi.getChildren(node);
        total += children.size();
        for(MegaNode child : children)
        {
            if (child.isFolder())
            {
                collectChildren(writer, child);
            }
            else if (child.isFile())
            {
                writer.append("//" + Constants.SBMEGA + "/" + email + megaApi.getNodePath(child));
                writer.newLine();
            }
            current ++;
            publishProgress(Tools.getProgress(total, current));
        }
	}

    @Override
    protected void onProgressUpdate(Integer[] values)
    {
        callback.progress(values[0]);
    }

    @Override
    protected void onPostExecute(String result)
    {
        callback.result(result);
    }
}
