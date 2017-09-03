package ru.net.serbis.mega;

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
    
    int ACTION_MOVE = 100;
    int ACTION_SELECT_PATH = 101;
    int ACTION_SELECT_ACCOUNT_PATH = 102;
    int ACTION_GET_FILES_LIST = 103;
}
