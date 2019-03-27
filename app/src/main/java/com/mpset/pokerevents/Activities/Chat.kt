package com.mpset.pokerevents.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateFormat
import android.view.KeyEvent
import android.widget.Toast
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.mpset.pokerevents.Adapters.ChatListAdapter
import com.mpset.pokerevents.Helper.DBHelper
import com.mpset.pokerevents.Helper.SoketHelper.Companion.socket
import com.mpset.pokerevents.Model.ChatModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userId
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONException
import org.json.JSONObject

import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.*


class Chat : AppCompatActivity() {

    val messagesChat= ArrayList<ChatModel>()
    lateinit var adapterChat: ChatListAdapter
    private var payload: JSONObject? = null
     var friendId:Int = 0
     var coversationId:Int = 0
     var friendAvatar:String =""
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        dbHelper = DBHelper(this)

        supportActionBar!!.title = intent.getStringExtra("friend_name")
         friendId = intent.getIntExtra("friend_id",0)
        coversationId = intent.getIntExtra("con_id",0)
        friendAvatar = intent.getStringExtra("friend_avatar")
//        Toast.makeText(applicationContext,friendId.toString(),Toast.LENGTH_LONG).show()
//        Toast.makeText(applicationContext,friendAvatar.toString(),Toast.LENGTH_LONG).show()
        init()
        recycler_chat.layoutManager = LinearLayoutManager(this)


        adapterChat = ChatListAdapter(this,messagesChat, object : ChatListAdapter.MyClickListener {
            override fun onClick(position: Int) {
            }
            override fun onLongClick(position: Int) {
            }

        })
        recycler_chat.adapter = adapterChat
        messagesChat.clear()
        val chat = dbHelper.getSpecificChat(coversationId)
        if (chat.getCount() == 0) {
        } else if (chat.moveToFirst()) {
            while (!chat.isAfterLast()) {
                val conId = chat.getString(1)
                val message = chat.getString(2)
                val time = chat.getString(3)
                val type = chat.getString(4)
                val avatar = chat.getString(5)

                messagesChat.add(ChatModel(avatar,getDateFromTimestamp(time.toLong()), message,type.toInt()))
                chat.moveToNext()
                adapterChat.notifyDataSetChanged()
                if (adapterChat!= null)
                    scrollToBottom()
            }

        }
        send_message_chat.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = write_text_chat!!.text.toString().trim { it <= ' ' }
        if (message != "") {
            write_text_chat!!.setText("")
            messagesChat.add(ChatModel(userAvatar,getDateFromTimestamp(getCurrentTimestamp()),message,1))
            dbHelper.insertDataMessages(coversationId,message,getCurrentTimestamp(),1, userAvatar)
            val data = JSONObject()
            try {
                data.put("room",friendId)
                data.put("message",message)
                socket!!.emit("message", data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            adapterChat.notifyDataSetChanged()
           scrollToBottom()

        } else {
            Toast.makeText(this@Chat, "Please Enter Some Text First.", Toast.LENGTH_SHORT).show()
        }

    }
    private val connectEvent = object : Emitter.Listener {
        override fun call(vararg args: Any) {

            val data = JSONObject()
            val userInfo = JSONObject()
            try {
                userInfo.put("id", userId)
                userInfo.put("name", userNickName)
                userInfo.put("avatar", userAvatar)
                data.put("room",userId)
                data.put("user",userInfo)
                socket!!.emit("join_room", data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }
    private fun init() {


        val payLoadString = intent.getStringExtra("NotificationManager.payload")

        try {

//            Message.CHAT_MESSAGE_NOTIFICATIONS = false

//            payload = JSONObject(payLoadString)
//            val pm = SharedPreferencesManager(activity)
//            user_access_token = pm.getString("user_access_token")
//            friend_access_token = payload!!.getString("friend_access_token")
//            friendNumber = payload!!.getString("friendNumber")
//            request_id = payload!!.getInt("request_id")
//            friend_id = payload!!.getInt("friend_id")

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        socket!!.on("connect", connectEvent)
        socket!!.on("message", handleIncomingMessages)

        socket!!.connect()
    }
    private fun scrollToBottom() {
        recycler_chat!!.scrollToPosition(adapterChat!!.itemCount - 1)
    }
    private val handleIncomingMessages = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            if (this@Chat != null) {
                this@Chat!!.runOnUiThread {
                    val data = args[0] as JSONObject
                    val message: String
                    val imageText: String
                    try {
//                        println("Args: $args with json data: $data")
                        if (data.has("message")) {
                            message = data.getString("message").toString()
                            messagesChat.add(ChatModel(friendAvatar,getDateFromTimestamp(getCurrentTimestamp()),message,0))

                            dbHelper.insertDataMessages(coversationId,message,getCurrentTimestamp(),0,friendAvatar)
//                            adapterChat.notifyDataSetChanged()

                            scrollToBottom()
                        }

                    } catch (e: JSONException) {
                        // return;
                    }
                }
            }
        }
    }
    companion object {
        fun getCurrentTime():String{
            val date = Date()
            val strDateFormat = "yyyy-MM-dd hh:mm a"
            val dateFormat = SimpleDateFormat(strDateFormat)
            val formattedDate = dateFormat.format(date)
            return formattedDate
        }
        fun getCurrentTimestamp():Long{
            val time = System.currentTimeMillis()
            return time
        }

         fun getDateFromTimestamp(time: Long): String {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = time
            return DateFormat.format("dd/MM/yyyy hh:mm a", cal).toString()
//            val formatter =  SimpleDateFormat("yyyy-MM-dd HH:mm a")
//            val dateString = formatter.format( Date((time)))
//            return  dateString
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        socket!!.close()
        socket!!.disconnect()
    }

    override fun onPause() {
        super.onPause()
        finish()
        socket!!.close()
        socket!!.disconnect()
    }

    override fun onStop() {
        super.onStop()
        finish()
        socket!!.close()
        socket!!.disconnect()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            socket!!.close()
            socket!!.disconnect()
        }
        return super.onKeyDown(keyCode, event)
    }
}
