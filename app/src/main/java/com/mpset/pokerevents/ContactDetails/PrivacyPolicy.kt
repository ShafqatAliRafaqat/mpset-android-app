package com.mpset.pokerevents.ContactDetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        supportActionBar!!.title = "Privacy Policy"

        webview_privacy_policy!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webview_privacy_policy!!.loadUrl("https://www.websitepolicies.com/policies/view/70RGLAJn")
    }

}
