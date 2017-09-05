package ru.net.serbis.mega.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;
import nz.mega.sdk.*;
import ru.net.serbis.mega.*;

public class NodesAdapter extends ArrayAdapter<MegaNode>
{
	private static int layoutId = R.layout.node;
	private MegaApiAndroid megaApi;

	private class Holder
	{
		private ImageView thumbnail;
		private TextView fileName;
		private TextView fileSize;
	}

	public NodesAdapter(Context context, MegaApiAndroid megaApi)
	{
		super(context, layoutId);
		this.megaApi = megaApi;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{
		Holder holder;
		if (view == null)
		{
			view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
			holder = new Holder();
			holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			holder.fileName = (TextView) view.findViewById(R.id.filename);
			holder.fileSize = (TextView) view.findViewById(R.id.filesize);

			view.setTag(holder);
		}
		else
		{
			holder = (Holder) view.getTag();
		}

		MegaNode node = getItem(position);
		holder.fileName.setText(node.getName());

		if (node.isFile())
		{
			holder.thumbnail.setImageResource(R.drawable.file);
			holder.fileSize.setText(getSizeString(node.getSize()));
		}
		else
		{
			holder.thumbnail.setImageResource(R.drawable.folder);
			holder.fileSize.setText(getInfoFolder(node));
		}
		return view;
	}

	private String getSizeString(long size)
	{
		String result = "";
		DecimalFormat decf = new DecimalFormat("###.##");

		float KB = 1024;
		float MB = KB * 1024;
		float GB = MB * 1024;
		float TB = GB * 1024;

		if (size < KB)
		{
			result = size + " B";
		}
		else if (size < MB)
		{
			result = decf.format(size / KB) + " KB";
		}
		else if (size < GB)
		{
			result = decf.format(size / MB) + " MB";
		}
		else if (size < TB)
		{
			result = decf.format(size / GB) + " GB";
		}
		else
		{
			result = decf.format(size / TB) + " TB";
		}

		return result;
	}

	private String getInfoFolder(MegaNode node)
	{
		int numFolders = megaApi.getNumChildFolders(node);
		int numFiles = megaApi.getNumChildFiles(node);;

		StringBuffer info = new StringBuffer();
		if (numFolders > 0)
		{
			info.append(numFolders)
				.append(" ")
				.append(
					getContext().getResources().getQuantityString(
						R.plurals.num_folders, numFolders));
		}
		if (numFolders > 0 && numFiles > 0)
		{
			info.append(", ");
		}
		if (numFiles > 0)
		{
			info.append(numFiles)
				.append(" ")
				.append(
					getContext().getResources().getQuantityString(
						R.plurals.num_files, numFiles));
		}

		return info.toString();
	}
}
