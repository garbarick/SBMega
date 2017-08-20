package ru.net.serbis.mega.data;

public class TwoValues<First, Second>
{
    private First first; 
    private Second second;

    public TwoValues(First first, Second second)
    {
        this.first = first;
        this.second = second;
    }

    public First getFirst()
    {
        return first;
    }

    public Second getSecond()
    {
        return second;
    }
}
