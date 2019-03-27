package com.mpset.pokerevents.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen.Companion.userAccessToken
import com.mpset.pokerevents.SplashScreen.Companion.userAddress
import com.mpset.pokerevents.SplashScreen.Companion.userAvatar
import com.mpset.pokerevents.SplashScreen.Companion.userEmail
import com.mpset.pokerevents.SplashScreen.Companion.userFirstName
import com.mpset.pokerevents.SplashScreen.Companion.userGender
import com.mpset.pokerevents.SplashScreen.Companion.userId
import com.mpset.pokerevents.SplashScreen.Companion.userLastName
import com.mpset.pokerevents.SplashScreen.Companion.userNickName
import com.mpset.pokerevents.SplashScreen.Companion.userPhone
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_facebook_google_detail_page.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.io.IOException

class EditProfile : AppCompatActivity() {
    val PICK_IMAGE_REQUEST = 101
    private var filePath: Uri? = null
     var imageString: String = ""
    var avatar = ""
    var phone = ""
    var email = ""
    var address = ""
    var firstName = ""
    var lastName = ""
    var nickName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
         nickName = intent.getStringExtra("nick_name")
        supportActionBar!!.title = nickName
         firstName = intent.getStringExtra("first_name")
         lastName = intent.getStringExtra("last_name")
         avatar = intent.getStringExtra("avatar")
         email = intent.getStringExtra("email")
         phone = intent.getStringExtra("phone")
         address = intent.getStringExtra("address")
        Glide.with(applicationContext).load(avatar).into(image_upload_editprofile)
        edittext_nickname_editprofile.text = Editable.Factory.getInstance().newEditable(nickName)
        edittext_first_editprofile.text = Editable.Factory.getInstance().newEditable(firstName)
        edittext_last_editprofile.text = Editable.Factory.getInstance().newEditable(lastName)
        edittext_email_editprofile.text = Editable.Factory.getInstance().newEditable(email)
        edittext_phone_editprofile.text = Editable.Factory.getInstance().newEditable(phone)
        edittext_address_editprofile.text = Editable.Factory.getInstance().newEditable(address)
        change_photo_text.setOnClickListener {
            fileChoser()
        }
        btn_save_edit_profile.setOnClickListener {
            serverCallCheckUserData(userAccessToken)
        }
    }
    fun serverCallCheckUserData(tokenAccess: String) {
        if (imageString.equals("")){
            imageString = avatar
        }
        val URL = resources.getString(R.string.base_url) + "/user/details"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here

            val json = JSONObject(s)
            val objectData = json.getJSONObject("data")
           val  id = objectData.getString("id")
            val nickName = objectData.getString("nick_name")
            val firstName = objectData.getString("first_name")
            val lastName = objectData.getString("last_name")
            val email = objectData.getString("email")
            val photoUrl = objectData.getString("avatar")
            val phone = objectData.getString("phone")
            val gender = objectData.getString("gender")
            val address = objectData.getString("address")
            userId = id
            userNickName = nickName
            userFirstName = firstName
            userLastName = lastName
            userEmail = email
            userAvatar = photoUrl
            userPhone = phone
            userGender = gender
            userAddress = address
            Toast.makeText(applicationContext,"Your Profile updated Successfully...",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EditProfile,MyProfile::class.java))
        }, Response.ErrorListener { e ->
            Log.e("TAG", "Received an exception", e)
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json", "Authorization" to "Bearer " + tokenAccess)

            override fun getParams(): Map<String, String> = mapOf(
                    "nick_name" to edittext_nickname_editprofile.text.toString(),
                    "first_name" to edittext_first_editprofile.text.toString(),
                    "last_name" to edittext_last_editprofile.text.toString(),
                    "gender" to userGender,
                    "phone" to edittext_phone_editprofile.text.toString(),
                    "avatar" to imageString,
                    "email" to edittext_email_editprofile.text.toString(),
                    "address" to edittext_address_editprofile.text.toString().replace("\n"," ")
            )
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)

    }
    private fun fileChoser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            val imageStream = getContentResolver().openInputStream(filePath)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageString = ImageHelperJava.encodeImage(selectedImage)
            imageString = imageString.replace("\n", "")

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                image_upload_editprofile!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
