package ru.net.serbis.mega.service.action;

import android.os.*;
import java.io.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;

public class GetFilesList extends Action
{
	private String fileslist;

	public GetFilesList(App app, Message msg)
	{
		super(app, msg);
	}

	@Override
	public void onFetched(MegaRequestListenerInterface listener)
	{
		generateFilesList();
		logout();
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
			collectChild(writer, node);
			writer.flush();
		}
		catch (Throwable e)
		{
			Log.info(this, e);
		}
		finally
		{
			Utils.close(writer);
		}
	}
	
	private void collectChild(BufferedWriter writer, MegaNode node) throws Exception
	{
		for(MegaNode children : megaApi.getChildren(node))
		{
			if (children.isFolder())
			{
				collectChild(writer, children);
			}
			else if (children.isFile())
			{
				writer.append("//" + Constants.SBMEGA + "/" + email + megaApi.getNodePath(children));
				writer.newLine();
			}
		}
	}

	@Override
	public void onLogout()
	{
		sendResult(Constants.FILES_LIST, fileslist);
	}
}
