package com.example.bachesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB_Helper (context:Context):SQLiteOpenHelper(context,dbname,factory,version){

    companion object{
        internal  val dbname = "Baches"
        internal  val factory = null
        internal  val version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE baches (" +
                "latitude REAL," +
                "longitude REAL)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun add(longitude:Float, latitude:Float){
        val db = this.writableDatabase
        val contentValues =  ContentValues()
        contentValues.put("latitude",latitude)
        contentValues.put("longitude",longitude)
        db.insert("baches",null,contentValues)
    }

    val allData: Cursor
        get(){
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM baches", null)
            return res
        }
}