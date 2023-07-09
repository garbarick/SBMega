package ru.net.serbis.mega.task;

public interface FileListCallback
{
    void progress(int progress);
    void result(String data);
}
