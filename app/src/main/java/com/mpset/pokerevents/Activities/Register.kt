package com.mpset.pokerevents.Activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mpset.pokerevents.Helper.ImageHelperJava
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import com.mpset.pokerevents.SplashScreen
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
import kotlinx.android.synthetic.main.activity_agree_term_condition.*
import kotlinx.android.synthetic.main.activity_agree_term_condition.view.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.io.*

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val PICK_IMAGE_REQUEST = 101
    private var filePath: Uri? = null
    lateinit var downloadPath: String
    lateinit var imageUrl: String
    lateinit var imageString: String
    lateinit var gender: String
    lateinit var pass: String
    lateinit var email: String
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var repass: String
    lateinit var phone: String
    lateinit var nickName: String
    lateinit var token: String
    lateinit var id: String
    lateinit var avatar: String
    lateinit var myDialog: Dialog
    lateinit var address:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        supportActionBar!!.title = "Register Now"
        auth = FirebaseAuth.getInstance()
        myDialog =  Dialog(this)

//        btn_reg_back.setOnClickListener {
//            var intent = Intent(applicationContext, Login::class.java)
//            startActivity(intent)
//        }
        btn_register.setOnClickListener {
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        image_upload.setOnClickListener {
            fileChoser()
        }


        btn_register.setOnClickListener {
            progresbar_register.visibility = View.VISIBLE

            validation(it)
//                        startActivity(Intent(applicationContext, AgreeTermCondition::class.java))
//            ShowPopup(it)
//            if(pass.equals(repass)) {
//                auth.createUserWithEmailAndPassword(edittext_email_reg.text.toString(), edittext_pass_reg.text.toString())
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                // Sign in success, update UI with the signed-in user's information
//                                val user = auth.currentUser!!.uid
//                                Toast.makeText(applicationContext,"start uploading", Toast.LENGTH_LONG).show()
//                                uploadFile(user)
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_LONG).show()
//
//                            }
//
//                        }
//            }

        }
        male.setOnClickListener {
            male.setBackgroundResource(R.drawable.blackbar)
            female.setBackgroundResource(R.drawable.outlinebar)
            male.setTextColor(Color.parseColor("#ffffff"))
            female.setTextColor(Color.parseColor("#9f9f9e"))
            gender = "male"
        }
        female.setOnClickListener {
            male.setBackgroundResource(R.drawable.outlinebar)
            female.setBackgroundResource(R.drawable.blackbar)
            male.setTextColor(Color.parseColor("#9f9f9e"))
            female.setTextColor(Color.parseColor("#ffffff"))
            gender = "female"
        }


    }
     fun ShowPopup(v:View) {

        myDialog.setContentView(R.layout.activity_agree_term_condition)

        myDialog.show()


         myDialog.btn_agreed.setOnClickListener {

             myDialog.cancel()
             startActivity(Intent(this@Register,SmsVerification::class.java))
         }
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

                image_upload!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun serverCallRegister() {
        email = edittext_email_reg.text.toString()
        pass = edittext_pass_reg.text.toString()
        firstName = edittext_fname.text.toString()
        lastName = edittext_lname.text.toString()
        nickName = edittext_nickname_reg.text.toString()
        repass = edittext_repass_reg.text.toString()
        phone = edittext_number_reg.text.toString()
        val URL = resources.getString(R.string.base_url) +"/register"
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.POST, URL, Response.Listener { s ->
            // Your success code here
//            Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
            val obj = JSONObject(s)
            val userObject = obj.getJSONObject("user")
            token = obj.getJSONObject("auth").getString("access_token")
            id = userObject.getString("id")
            nickName = userObject.getString("nick_name")
            firstName = userObject.getString("first_name")
            lastName = userObject.getString("last_name")
            email = userObject.getString("email")
            avatar = userObject.getString("avatar")
            phone = userObject.getString("phone")
            gender = userObject.getString("gender")
            address = userObject.getString("address")
            saveUserToShareprefrences()
            progresbar_register.visibility = View.GONE
            startActivity(Intent(this@Register,MainActivity::class.java))
        }, Response.ErrorListener { e ->
            progresbar_register.visibility = View.GONE
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): Map<String, String> = mapOf("Accept" to "application/json")

            override fun getParams(): Map<String, String> = mapOf(
                    "email" to email,
                    "password" to pass,
                    "first_name" to firstName,
                    "last_name" to lastName
                    , "phone" to phone
                    , "gender" to gender
                    , "avatar" to imageString
                    , "nick_name" to nickName,
                    "address" to "")
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(6000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue.add(stringRequest)
    }

    fun validation(view: View) {

        if (edittext_nickname_reg.text.isEmpty()) {
            Snackbar.make(view, "Please enter nickname", Snackbar.LENGTH_LONG).show()
            return
        }
        if (edittext_fname.text.isEmpty()) {
            Snackbar.make(view, "Please enter firstname", Snackbar.LENGTH_LONG).show()
            return
        }
        if (edittext_lname.text.isEmpty()) {
            Snackbar.make(view, "Please enter lastname", Snackbar.LENGTH_LONG).show()
            return
        }
        if (edittext_email_reg.text.isEmpty()) {
            Snackbar.make(view, "Please enter email", Snackbar.LENGTH_LONG).show()
            return
        }

        if (edittext_pass_reg.text.isEmpty()) {
            Snackbar.make(view, "Please enter password", Snackbar.LENGTH_LONG).show()
            return
        }
        if (edittext_repass_reg.text.isEmpty()) {
            Snackbar.make(view, "Please enter re password", Snackbar.LENGTH_LONG).show()
            return
        }
        if (edittext_number_reg.text.isEmpty()) {
            Snackbar.make(view, "Please enter number", Snackbar.LENGTH_LONG).show()
            return
        }
                serverCallRegister ()
    }
    private  fun saveUserToShareprefrences( ){
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token",token)
        editor.putString("id",id)
        editor.putString("nick_name",nickName)
        editor.putString("first_name",firstName)
        editor.putString("last_name",lastName)
        editor.putString("avatar",avatar)
        editor.putString("email",email)
        editor.putString("phone",phone)
        editor.putString("gender",gender)
        editor.putString("address",address)
        editor.commit()
        userAccessToken = token
        userId = id
        userNickName = nickName
        userFirstName = firstName
        userLastName = lastName
        userEmail = email
        userAvatar = avatar
        userPhone = phone
        userGender = gender
        userAddress = address
    }
}
