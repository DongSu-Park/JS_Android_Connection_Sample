package com.example.webview_connect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        wv_welcome.run {
            settings.javaScriptEnabled = true
            loadUrl("file:///android_asset/welcome.html")
        }

        // js -> native (웹 코드에서 안드로이드 동작을 유도)
        wv_welcome.addJavascriptInterface(object : JsCallBack {
            @SuppressLint("CheckResult")
            @JavascriptInterface
            override fun webViewToApp(value: String?) {
                Single.just(value)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            tv_return.text = it
                        },
                        {
                            Log.d("TAG", "error code : $it")
                        }
                    )
            }
        }, "WebViewCallbackInterface")

        // native -> js (안드로이드 코드에서 웹 동작을 유도)
        btn_nativeCallBack.setOnClickListener {
            val textMessage: String = et_text.text.toString()
            wv_welcome.evaluateJavascript("javascript:appToWebView('$textMessage')") {
                // 콜백
                    value ->
                Toast.makeText(this, "js 전송 성공 : $value", Toast.LENGTH_LONG).show()
            }
        }
    }

}

