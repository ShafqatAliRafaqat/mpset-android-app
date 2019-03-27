package com.mpset.pokerevents.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.mpset.pokerevents.MainActivity
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_sms_verification.*

class SmsVerification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_verification)
        supportActionBar!!.title = "Verification"
        btn_ver_code.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        edittext_ver_code1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edittext_ver_code1.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittext_ver_code1.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1) {
                    edittext_ver_code2.requestFocus()
                    edittext_ver_code2.setBackgroundResource(R.drawable.goldcircle)
                }
            }
        })
        edittext_ver_code2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edittext_ver_code2.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittext_ver_code2.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1) {
                    edittext_ver_code3.requestFocus()
                    edittext_ver_code3.setBackgroundResource(R.drawable.goldcircle)
                }
            }
        })
        edittext_ver_code3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                edittext_ver_code3.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittext_ver_code3.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 1) {
                    edittext_ver_code4.requestFocus()
                    edittext_ver_code3.setBackgroundResource(R.drawable.goldcircle)
                }
            }
        })
        edittext_ver_code4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Toast.makeText(applicationContext,"Congrats",Toast.LENGTH_LONG).show()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittext_ver_code4.setBackgroundResource(R.drawable.goldcircle)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittext_ver_code4.setBackgroundResource(R.drawable.goldcircle)

            }
        })
        text_resent.setOnClickListener {
//            Toast.makeText(applicationContext,"sent",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@SmsVerification,ForgotPassword::class.java))

        }
    }
}
