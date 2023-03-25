package com.karan.bunchtest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EmployeeDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "EmployeeDetails";

    private static final String ID_COL = "EmpID";

    private static final String NAME_COL = "EmpName";

    // creating a constructor for our database handler.
    public EmployeeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT"+")";

        db.execSQL(query);
    }

    public void addNewCourse(String EmpName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COL, EmpName);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

