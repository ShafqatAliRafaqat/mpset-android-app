package com.mpset.pokerevents.ContactDetails

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mpset.pokerevents.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        supportActionBar!!.title = "Terms And Conditions"
        webview_terms_conditions!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webview_terms_conditions!!.loadUrl("https://www.websitepolicies.com/policies/view/BF4arh0A")
    }
}
