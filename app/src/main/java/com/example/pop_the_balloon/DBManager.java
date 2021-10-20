package com.example.pop_the_balloon;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class DBManager {

    private SQLiteDatabase sqlDB;
    private static final String DBName = "The Highest Score";
    private static final String TName = "LeaderBoard";
    public static final String Name = "Name";
    public static final String Score = "Score";
    private static final int DBVersion = 1;

    private static final String CreateTable = "Create table IF NOT EXISTS " +TName+
            "(ID integer PRIMARY KEY AUTOINCREMENT,"+ Name+
            " text,"+ Score + " int);";

    static class DatabaseHelperUser extends SQLiteOpenHelper {

        Context context;
        DatabaseHelperUser(Context context) {
            super(context,DBName,null,DBVersion);
            this.context=context;
        }

        //initiate table
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CreateTable);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF EXISTS "+ TName);
            onCreate(db);
        }
    }

    // constructor , updates sqlDB param so we could use it
    public DBManager(Context context) {
        DatabaseHelperUser db = new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }

    // insert a value to the table
    void Insert(ContentValues values) {
        sqlDB.insert(TName, "", values);
    }

    // returns the wanted columns desc order
    public Cursor Query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TName);

        return queryBuilder.query(sqlDB, Projection, Selection, SelectionArgs,
                null, null, SortOrder+" DESC");

    }

}