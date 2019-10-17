package com.mjgl.utla.utla;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GestionSQLiteOpenHelper extends SQLiteOpenHelper {

    public GestionSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version){
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table empleados(codigo integer primary key, nombre text, importe double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists empleados");
        db.execSQL("create table empleados(codigo integer primary key, nombre text, importe double)");
    }
}
