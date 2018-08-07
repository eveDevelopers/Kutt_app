package com.example.root.kutt_app_i;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DBNAME="first.db";
    public static final String TBNAME="firstEntry";
    public static final String COL1="ID";
    public static final String COL2="LINK";
    public static final String COL3 ="STAR";
    public static final String TITLE ="TITLE";
    public static final String ICON ="ICON";
    public DatabaseHelper(Context context) {
        super(context,DBNAME , null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TBNAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LINK TEXT,STAR INT DEFAULT 0,TITLE TEXT,ICON BLOB)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData (String link){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        deletelink(link);
        contentValues.put(COL2, link);


        long result = sqLiteDatabase.insert(TBNAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }
    public boolean deletelink(String link)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TBNAME,COL2 +"="+"'"+link+"'" ,null) > 0;
    }
    public boolean check_info(String link){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select * from " + TBNAME +  " where "+ COL2 +"='"+link+"' and "+ICON+" is not null",null);
        return res.getCount() > 0;
    }
    public boolean add_icon(byte[] icon,String link){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ICON,icon);
        return  sqLiteDatabase.update(TBNAME,contentValues,COL2 +"="+"'"+link+"'" ,null) > 0;
    }
    public boolean add_title(String title,String link){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,title);
        return  sqLiteDatabase.update(TBNAME,contentValues,COL2 +"="+"'"+link+"'" ,null) > 0;
    }
    public Cursor getAllData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select * from " + TBNAME +  " order By "+ COL1 +" DESC ",null);

        return  res;
    }
    public  Cursor preview_data(int pos){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select LINK from " + TBNAME + " where "+ COL1 + " =" + pos ,null);
        return  res;

    }
    public  int getStar(int pos){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select STAR from " + TBNAME + " where "+ COL1 + " =" + pos ,null);
        res.moveToFirst();
        return  res.getInt(0);

    }
    public  boolean updateData(String link){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3, 1);
        sqLiteDatabase.update(TBNAME,contentValues, "LINK = ?",new String[]{ link });

        return true;


    }

    public  boolean updateNormal(String link){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3, 0 );
        sqLiteDatabase.update(TBNAME,contentValues, "LINK = ?",new String[]{ link });

        return true;


    }
    public Cursor getUpdateData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select * from " + TBNAME + " where "+ COL3 + " =" + 1 + " order By "+ COL1 +" DESC ",null);

        return  res;
    }







}
