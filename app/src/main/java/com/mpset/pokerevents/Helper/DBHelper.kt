package com.mpset.pokerevents.Helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mpset.pokerevents.SplashScreen.Companion.userId

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){


    companion object {
        private val DATABASE_VERSION = 1
        private  val DATABASE_NAME = "CHAT.db"
        //Table1
        private val TABLE_NAME_USER = "User"
        private val USER_ID = "Id"
        private val USER_NAME = "Name"
        private val USER_AVATAR = "Avatar"
        private val USER_DATE_TIME = "Time"
        //Table2
        private val TABLE_NAME_CONVERSATION = "Conversation"
        private val CON_ID = "Id"
        private val SENDER_ID = "Sender_id"
        private val RECIEVER_ID = "Receiver_id"
        private val CON_DATE_TIME = "Time"
        //Table3
        private val TABLE_NAME_MESSAGES= "Messages"
        private val MSG_ID = "Id"
        private val CON_MSG_ID = "Con_id"
        private val TEXT = "Text"
        private val MSG_DATE_TIME = "Time"
        private val MSG_TYPE = "TYPE"
        private val MSG_AVATAR= "AVATAR"

    }



    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY_USER = "CREATE TABLE " + TABLE_NAME_USER +" (" + USER_ID +" INTEGER PRIMARY KEY," +
                USER_NAME + " TEXT," + USER_AVATAR + " TEXT," +
                USER_DATE_TIME +" TEXT)"
        val CREATE_TABLE_QUERY_CON = "CREATE TABLE " + TABLE_NAME_CONVERSATION +" (" + CON_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                SENDER_ID + " INTEGER," + RECIEVER_ID + " INTEGER," +
                CON_DATE_TIME +" TEXT)"
        val CREATE_TABLE_QUERY_MSG = "CREATE TABLE " + TABLE_NAME_MESSAGES +" (" + MSG_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                CON_MSG_ID + " INTEGER," + TEXT + " TEXT," +
        MSG_DATE_TIME +" LONG," + MSG_TYPE +  " INTEGER," + MSG_AVATAR + " TEXT)"
        db!!.execSQL(CREATE_TABLE_QUERY_USER)
        db!!.execSQL(CREATE_TABLE_QUERY_CON)
        db!!.execSQL(CREATE_TABLE_QUERY_MSG)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME_USER)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONVERSATION)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MESSAGES)
        onCreate(db!!)
      }

    fun insertDataUser( id:Int,name: String, avatar: String, time: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_ID, id)
        values.put(USER_NAME, name)
        values.put(USER_AVATAR, avatar)
        values.put(USER_DATE_TIME, time)
        db.insert(TABLE_NAME_USER, null, values)

    }

    fun insertDataConversation( senderId: Int, receiverId: Int, time: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SENDER_ID, senderId)
        values.put(RECIEVER_ID, receiverId)
        values.put(CON_DATE_TIME, time)
        db.insert(TABLE_NAME_CONVERSATION, null, values)

    }

    fun insertDataMessages( conMsgId: Int, text: String, time: Long,type: Int,avatar:String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CON_MSG_ID, conMsgId)
        values.put(TEXT, text)
        values.put(MSG_DATE_TIME, time)
        values.put(MSG_TYPE, type)
        values.put(MSG_AVATAR, avatar)
        db.insert(TABLE_NAME_MESSAGES, null, values)

    }

    fun   checkIsDataAlreadyInDBorNot(senderId:Int, receiverId:Int):Boolean {
        val db = this.writableDatabase
        val Query = "Select * from " + TABLE_NAME_CONVERSATION + " where " + SENDER_ID + " = " + senderId + " and " + RECIEVER_ID + " = " + receiverId
        val cursor = db.rawQuery(Query, null)
        if(cursor.getCount() <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }
    fun   checkIsUserAlreadyInDBorNot(uId:Int):Boolean {
        val db = this.writableDatabase
        val Query = "Select * from " + TABLE_NAME_USER + " where " + USER_ID + " = " + uId
        val cursor = db.rawQuery(Query, null)
        if(cursor.getCount() <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun getConversationId(senderId:Int, receiverId:Int): Cursor {

        val database = this.writableDatabase
        return database.rawQuery("SELECT Id FROM " + TABLE_NAME_CONVERSATION+ " where " + SENDER_ID + " = " + senderId + " and " + RECIEVER_ID + " = " + receiverId, null)
    }

    fun getSpecificChat(conId:Int): Cursor {

        val database = this.writableDatabase
        return database.rawQuery("SELECT * FROM "+ TABLE_NAME_MESSAGES + " where " + CON_MSG_ID + " = " + conId, null)
    }
    fun getInboxChat(): Cursor {

        val sql = "SELECT "+ TABLE_NAME_USER+".id,"+ TABLE_NAME_USER+".name,"+ TABLE_NAME_USER+".avatar,"+ TABLE_NAME_CONVERSATION+".id as con_id,ms.Text,ms.time " +
                "FROM "+TABLE_NAME_CONVERSATION +
                " left join "+ TABLE_NAME_USER+" on "+ TABLE_NAME_CONVERSATION+".receiver_id == "+ TABLE_NAME_USER+".id " +
                " left join " +
                "(SELECT * FROM "+ TABLE_NAME_MESSAGES + " group by "+ TABLE_NAME_MESSAGES + ".con_id order by "+ TABLE_NAME_MESSAGES + ".time asc ) as ms on ms.Con_id == "+ TABLE_NAME_CONVERSATION+".id where " + TABLE_NAME_CONVERSATION+".sender_id == " + userId

        val database = this.writableDatabase
        return database.rawQuery(sql, null)
    }
}
