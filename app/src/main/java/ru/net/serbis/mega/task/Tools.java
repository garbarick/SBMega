package ru.net.serbis.mega.task;

import nz.mega.sdk.*;

public class Tools
{
	public static int getProgress(MegaRequest request)
	{
		return getProgress(request.getTotalBytes(), request.getTransferredBytes());
	}
	
	public static int getProgress(MegaTransfer request)
	{
		return getProgress(request.getTotalBytes(), request.getTransferredBytes());
	}
	
	private static int getProgress(long total, long current)
	{
		if (total > 0)
		{
			double result = 100 * current / total;
			if (result > 100 || result < 0)
			{
				result = 100;
			}
			return (int) result;        
		}
		return 0;
	}
}
