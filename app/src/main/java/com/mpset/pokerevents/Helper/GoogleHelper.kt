package com.mpset.pokerevents.Helper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mpset.pokerevents.Activities.GoogleDetailPage

class GoogleHelper(context: Context)  {
//        object googleDetailPage
//        val RC_SIGN_IN: Int = 1
//        lateinit var gso: GoogleSignInOptions
//        lateinit var mGoogleSignInClient: GoogleSignInClient
//
//
//
//    fun signInCall(context: Context) {
//             val serverClientId ="669317931495-en79bhq0ugoch00kteguf178bs4m70ep.apps.googleusercontent.com"
//             gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                     .requestIdToken(serverClientId)
////                .requestScopes( Scope(Scopes.DRIVE_APPFOLDER))
////                .requestServerAuthCode("516394471224-r7gu78u9u784tku02d9212fgm5gc4rda.apps.googleusercontent.com")
//                     .requestEmail()
//                     .build()
//             mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
//             signIn()
//
//        }
//    private fun signIn () {
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//
//            handleResult (task)
//        }else {
//            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
//        }
//    }
//    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
//            updateUI (account )
//        } catch (e: ApiException) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
//        }
//    }
//    private fun updateUI (account: GoogleSignInAccount): GoogleSignInAccount {
//        return account
//    }


}