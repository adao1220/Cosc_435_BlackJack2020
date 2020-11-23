package com.example.blackjack2020.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.blackjack2020.SettingsActivity
import com.example.blackjack2020.SettingsActivity.Companion.ProfileName
import com.example.blackjack2020.SettingsActivity.Companion.TotalFunds
import com.example.blackjack2020.SettingsActivity.Companion.card
import com.example.blackjack2020.SettingsActivity.Companion.difficulty
import com.example.blackjack2020.models.SettingModel

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        //creating the table
        //Watch out, if you change anything in this code, you might need to change the DATABASE_VERSION...DONT MESS WITH IT
        val CREATE_USER_TABLE = (
                "CREATE TABLE "
                + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AI + " TEXT,"
                + KEY_CARD + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_FUNDS + " REAL,"
                + KEY_MUSIC + " TEXT"
                + ")")
        Log.i(TAG, CREATE_USER_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
    }

    fun addUser(set: SettingModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_AI, set.difficulty)
        contentValues.put(KEY_CARD, set.card)
        contentValues.put(KEY_NAME, set.profileName)
        contentValues.put(KEY_FUNDS, TotalFunds)
        Log.i(SettingsActivity.TAG+ "add" , set.card.toString())
        Log.i(SettingsActivity.TAG+ "add" , set.difficulty.toString())
        Log.i(SettingsActivity.TAG+ "add" , set.profileName.toString())
        Log.i(SettingsActivity.TAG+ "add" , TotalFunds.toString())
        val success = db.insert(TABLE_USER, null, contentValues)
        Log.i(TAG, success.toString())
        db.close()
        return success
    }

    fun deleteUser(set: SettingModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, set.id)
        // Deleting Row
        val success = db.delete(TABLE_USER, KEY_ID + "=" + set.id, null)
        db.close()
        return success
    }
    fun updateUser(set: SettingModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_AI, set.difficulty)
        contentValues.put(KEY_CARD, set.card)
        contentValues.put(KEY_NAME, set.profileName)
        contentValues.put(KEY_FUNDS, TotalFunds)
        Log.i(SettingsActivity.TAG+ "edit" , set.card.toString())
        Log.i(SettingsActivity.TAG+ "edit" , set.difficulty.toString())
        Log.i(SettingsActivity.TAG+ "edit" , set.profileName.toString())
        Log.i(SettingsActivity.TAG+ "edit" , TotalFunds.toString())
        // Updating Row
        val success = db.update(TABLE_USER, contentValues, KEY_ID + "=" + set.id, null)
        db.close()
        return success
    }

    fun getUser(set: SettingModel): ArrayList<SettingModel>{
        val setList: ArrayList<SettingModel> = ArrayList<SettingModel>()
        val selectQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + KEY_ID + "=" + set.id

        val db = this.readableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var ai : String
        var card : String
        var name: String
        var funds : Double

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                ai = cursor.getString(cursor.getColumnIndex(KEY_AI))
                card = cursor.getString(cursor.getColumnIndex(KEY_CARD))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                funds = cursor.getDouble(cursor.getColumnIndex(KEY_FUNDS))
                Log.i(SettingsActivity.TAG+ "load" , id.toString())
                Log.i(SettingsActivity.TAG+ "load" , card.toString())
                Log.i(SettingsActivity.TAG+ "load" , name.toString())
                Log.i(SettingsActivity.TAG+ "load" , funds.toString())
                Log.i(SettingsActivity.TAG+ "load" , ai.toString())

                val set = SettingModel(id = id, difficulty = ai, card = card, profileName = name, funds = funds)

                setList.add(set)

            } while (cursor.moveToNext())
        }
        return setList
    }


    fun viewfirst(): ArrayList<SettingModel> {
        val setList: ArrayList<SettingModel> = ArrayList<SettingModel>()
        val selectQuery = "SELECT  * FROM $TABLE_USER"

        val db = this.readableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var ai : String
        var card : String
        var name: String
        var funds : Double

        if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                ai = cursor.getString(cursor.getColumnIndex(KEY_AI))
                card = cursor.getString(cursor.getColumnIndex(KEY_CARD))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                funds = cursor.getDouble(cursor.getColumnIndex(KEY_FUNDS))

                val set = SettingModel(id = id, difficulty = ai, card = card, profileName = name, funds = funds)
                setList.add(set)

        }
        return setList
    }


    fun viewAll(): ArrayList<SettingModel> {
        val setList: ArrayList<SettingModel> = ArrayList<SettingModel>()
        val selectQuery = "SELECT  * FROM $TABLE_USER"

        val db = this.readableDatabase
        var cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var ai : String
        var card : String
        var name: String
        var funds : Double

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                ai = cursor.getString(cursor.getColumnIndex(KEY_AI))
                card = cursor.getString(cursor.getColumnIndex(KEY_CARD))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                funds = cursor.getDouble(cursor.getColumnIndex(KEY_FUNDS))

                val set = SettingModel(id = id, difficulty = ai, card = card, profileName = name, funds = funds)
                setList.add(set)

            } while (cursor.moveToNext())
        }
        return setList
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "UserInfoDatabase"
        private val TABLE_USER = "UsersTable"
        private val KEY_ID = "_id"
        private val KEY_AI = "difficulty"
        private val KEY_CARD = "card"
        private val KEY_NAME = "name"
        private val KEY_FUNDS = "funds"
        private val KEY_MUSIC = "music"
        val TAG = "TESTING NOW"

    }

}