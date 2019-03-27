package com.mpset.pokerevents.ContactDetails

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_contact_us.*

class ContactUs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        supportActionBar!!.title = "Contact Us"
        link_site_contact.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL,  "support@mpset.co" )
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on MPSET app from user ")
            intent.putExtra(Intent.EXTRA_TEXT, "Please share with us any feedback you might have\n" +
                    "Username id: <Username id>\n" +
                    "Device type: <device>\n" +
                    "System version: <sys ver.>\n" +
                    "Application version: <app ver.>\n")
            intent.setType("text/plain")
            intent.setData(Uri.parse("support@mpset.co"))
            startActivity(Intent.createChooser(intent, "Send Email using:"))
        }
    }
}
