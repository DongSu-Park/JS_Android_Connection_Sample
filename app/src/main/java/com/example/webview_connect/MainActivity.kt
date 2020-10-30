package com.example.webview_connect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        wv_welcome.run{
            settings.javaScriptEnabled = true
            loadUrl("file:///android_asset/welcome.html")
        }

        wv_welcome.addJavascriptInterface(object : JsCallBack{
            @JavascriptInterface
            override fun webViewToApp(value: String?) {
                // js -> native 로 온 결과값 세팅
                tv_return.text = value
            }
        }, "WebViewCallbackInterface")

        btn_nativeCallBack.setOnClickListener {
            val textMessage : String = et_text.text.toString()
            wv_welcome.evaluateJavascript("javascript:appToWebView('$textMessage')") {
                value -> Toast.makeText(this, "js 전송 성공 : $value", Toast.LENGTH_LONG).show()
            }
        }
    }

}

