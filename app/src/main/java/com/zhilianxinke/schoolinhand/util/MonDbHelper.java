package com.zhilianxinke.schoolinhand.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MonDbHelper
  extends SQLiteOpenHelper
{
  private static final int DATABASE_VERSION = 1;
  private static String dbName = "monitor.db";
  
  public MonDbHelper(Context paramContext)
  {
    super(paramContext, dbName, null, 1);
  }
  
  public MonDbHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS monitor(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, url VARCHAR,code VARCHAR,status INT,nickName VARCHAR)");
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}
}
