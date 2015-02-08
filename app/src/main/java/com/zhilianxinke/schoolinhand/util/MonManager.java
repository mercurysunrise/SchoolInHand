package com.zhilianxinke.schoolinhand.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class MonManager
{
  private SQLiteDatabase db;
  private MonDbHelper helper;
  
  public MonManager(Context paramContext)
  {
    this.helper = new MonDbHelper(paramContext);
    this.db = this.helper.getWritableDatabase();
  }
  
  public void Add(Monitor paramMonitor)
  {
    SQLiteDatabase localSQLiteDatabase = this.db;
    Object[] arrayOfObject = new Object[5];
    arrayOfObject[0] = paramMonitor.getName();
    arrayOfObject[1] = paramMonitor.getUrl();
    arrayOfObject[2] = paramMonitor.getCode();
    arrayOfObject[3] = Integer.valueOf(paramMonitor.getStatus());
    arrayOfObject[4] = paramMonitor.getNickName();
    localSQLiteDatabase.execSQL("INSERT INTO monitor VALUES(null, ?, ?, ?, ?, ?)", arrayOfObject);
  }
  
  public void closeDB()
  {
    this.db.close();
  }
  
  public void deleteMonitor(Monitor paramMonitor)
  {
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramMonitor.getUrl());
    localSQLiteDatabase.delete("monitor", "url = ?", arrayOfString);
  }
  
  public ArrayList<Monitor> query()
  {
    ArrayList localArrayList = new ArrayList();
    Cursor localCursor = queryTheCursor();
    for (;;)
    {
      if (!localCursor.moveToNext())
      {
        localCursor.close();
        return localArrayList;
      }
      Monitor localMonitor = new Monitor();
      localMonitor.setId(localCursor.getInt(localCursor.getColumnIndex("_id")));
      localMonitor.setName(localCursor.getString(localCursor.getColumnIndex("name")));
      localMonitor.setUrl(localCursor.getString(localCursor.getColumnIndex("url")));
      localMonitor.setCode(localCursor.getString(localCursor.getColumnIndex("code")));
      localMonitor.setStatus(localCursor.getInt(localCursor.getColumnIndex("status")));
      localMonitor.setNickName(localCursor.getString(localCursor.getColumnIndex("nickName")));
      localArrayList.add(localMonitor);
    }
  }
  
  public Cursor queryTheCursor()
  {
    return this.db.rawQuery("SELECT * FROM monitor", null);
  }
  
  public void updateMonitor(Monitor paramMonitor)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("name", paramMonitor.getName());
    localContentValues.put("url", paramMonitor.getUrl());
    localContentValues.put("code", paramMonitor.getCode());
    localContentValues.put("status", Integer.valueOf(paramMonitor.getStatus()));
    localContentValues.put("nickName", paramMonitor.getNickName());
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = String.valueOf(paramMonitor.getId());
    localSQLiteDatabase.update("monitor", localContentValues, "_id = ?", arrayOfString);
  }
}

