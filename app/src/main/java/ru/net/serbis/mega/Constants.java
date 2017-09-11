package ru.net.serbis.mega;

import java.util.regex.*;

public interface Constants
{
	String TYPE = "ru.net.serbis.mega";
	String TOKEN = TYPE + ".TOKEN";
	String TOKEN_TYPE = TYPE + ".TOKEN_TYPE";
    String ACCOUNT = TYPE + ".ACCOUNT";
	String SELECT_MODE = TYPE + ".SELECT_MODE";
	String SELECT_PATH = TYPE + ".SELECT_PATH";
    String PATH = TYPE + ".PATH";
    String ACTION = TYPE + ".ACTION";
    String FILES_LIST = TYPE + ".FILES_LIST";
	String FILE = TYPE + ".FILE";
	String ERROR = TYPE + ".ERROR";
	String RESULT = TYPE + ".RESULT";
	
	String SBMEGA = "sbmega";
    String SUCCESS = "SUCCESS";
	
    int ACTION_MOVE = 100;
    int ACTION_SELECT_PATH = 101;
    int ACTION_SELECT_ACCOUNT_PATH = 102;
    int ACTION_GET_FILES_LIST = 103;
	int ACTION_GET_FILE = 104;
	int ACTION_REMOVE_FILE = 105;
	
	Pattern PATH_PATTERN = Pattern.compile("^\\/\\/" + SBMEGA + "\\/(.*?)(\\/.*)$");
}
