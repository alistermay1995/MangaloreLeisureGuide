package com.mlg.mangaloreleisureguide;

public class Restaurant
{
    private String addr;
    private String desc;
    private String name;
    private String rate;
    private String tim;
    private String loc_lat;
    private String loc_long;

    public Restaurant()
    {

    }

    public Restaurant(String addr, String desc, String name, String rate, String tim, String loc)
    {
        this.addr = addr;
        this.desc = desc;
        this.name = name;
        this.rate = rate;
        this.tim = tim;
        this.loc_lat = loc;
    }
    public String retAddr()
    {
        return addr;
    }
    public String retDesc()
    {
        return desc;
    }
    public String retName()
    {
        return name;
    }
    public String retRate()
    {
        return rate;
    }
    public String retTime()
    {
        return tim;
    }
    public String retLoc()
    {
        return loc_lat;
    }


}
