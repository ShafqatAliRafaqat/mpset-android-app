package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.mpset.pokerevents.Activities.Chat.Companion.getDateFromTimestamp
import com.mpset.pokerevents.Adapters.InProgressListAdapter
import com.mpset.pokerevents.Adapters.InboxListAdapter
import com.mpset.pokerevents.Helper.DBHelper
import com.mpset.pokerevents.Model.ChatModel
import com.mpset.pokerevents.Model.InProgressBottom
import com.mpset.pokerevents.Model.InboxModel
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_inbox.*
import java.util.ArrayList

class Inbox : AppCompatActivity() {
    val usersInbox = ArrayList<InboxModel>()
    lateinit var adapterInbox: InboxListAdapter
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        supportActionBar!!.title = "Inbox"
        dbHelper = DBHelper(this)
        val linearLayout = LinearLayoutManager(this,LinearLayout.VERTICAL,true)
        linearLayout.stackFromEnd = true
        recycler_inbox.layoutManager = linearLayout


        adapterInbox = InboxListAdapter(this,usersInbox, object : InboxListAdapter.MyClickListener {
            override fun onClick(position: Int) {
//                Toast.makeText(, users[position].id, Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,Chat::class.java)
                intent.putExtra("friend_name", usersInbox[position].userName)
                intent.putExtra("friend_id", usersInbox[position].friendId)
                intent.putExtra("con_id", usersInbox[position].id)
                intent.putExtra("friend_avatar", userAvatar)
                startActivity(intent)


            }

            override fun onLongClick(position: Int) {
//                Toast.makeText(context, "You got a long click it", Toast.LENGTH_LONG).show()
            }

        })
        recycler_inbox.adapter = adapterInbox
        fab.setOnClickListener {
            val intent = Intent(this@Inbox,MyFriends::class.java)
            startActivity(intent)
        }

        val chat = dbHelper.getInboxChat()
        if (chat.getCount() == 0) {
        } else if (chat.moveToFirst()) {
            while (!chat.isAfterLast()) {
                val conId = chat.getInt(3)
                val friendId = chat.getInt(0)
                val name = chat.getString(1)
                val time = chat.getString(5)
                val text = chat.getString(4)
                val url = chat.getString(2)
                if(text!=null)
                usersInbox.add(InboxModel(conId,name,text, getDateFromTimestamp(time.toLong()), url,friendId))
                chat.moveToNext()
            }
            adapterInbox.notifyDataSetChanged()
//            scrollToBottom()
        }
    }
//    private fun scrollToBottom() {
//        recycler_chat!!.scrollToPosition(adapterInbox!!.userList.size - 1)
//    }
}
