package com.kapilguru.trainer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.check_sample.*
import org.jitsi.meet.sdk.EntryActivity
import java.lang.Exception

class SampleActivity : AppCompatActivity() {

    val urlCheck ="https://teams.microsoft.com/l/meetup-join/19%3ameeting_Y2I2NDIwNzYtNTI1ZC00NDZmLWIxNWMtOGRkMzU0N2RiMTJm%40thread.v2/0?context=%7b%22Tid%22%3a%22e6f33f84-d25e-484b-b0c0-c22b98f1d1f9%22%2c%22Oid%22%3a%220851f0fe-2b75-4b65-9869-72fc00ea7972%22%7d"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.sample_details)
        setContentView(R.layout.check_sample)
//        sample.setWebViewClient(myWebViewClient(this))
//        sample.getSettings().setJavaScriptEnabled(true)

        buttonok.setOnClickListener {
            try{
                EntryActivity.launch(this,"BT16917","","kg_h00001","12345"); //trainer
//                EntryActivity.launch(this,"BT16917","Student 1");  //student
            }catch (e : Exception)
            {
                e.printStackTrace();
            }
        }

    }

   inner class myWebViewClient(var activity: Activity) : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(intent)
            try {
                view.loadUrl(urlCheck)
            }catch (e: Exception) {
                Log.d("checkVIdeo", "shouldOverrideUrlLoading: ${e.printStackTrace()}")
            }

            return true
        }
    }

}
