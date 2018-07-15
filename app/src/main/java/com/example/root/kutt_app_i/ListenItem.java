package com.example.root.kutt_app_i;

public class ListenItem {
    private String Link,Title;
    private int Star,Pos_in_db;
    private byte[] Icon;


    public ListenItem(int position,String link ,int star,String title,byte[] icon) {
        Pos_in_db = position;
        Link = link;
        Title = title;
        Star = star;
        Icon = icon;
    }
    public int getPos_in_db(){
        return Pos_in_db;
    }
    public String getLink(){
        return Link;
    }
    public String getTitle(){
        return Title;
    }
    public int getStar(){
        return Star;
    }
    public byte[] getIcon(){
        return Icon;
    }


}