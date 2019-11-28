package com.example.safehomecoming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class Activity_CCTV extends AppCompatActivity
{

    String IP = "192.168.0.56";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cctv);

        webView = findViewById(R.id.webView_cctv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebBridge(), "java");
        // set the scale 웹뷰 사이즈 조절
//        webView.setInitialScale(320); // 330 == 330%

//        String path = "http://" + IP + ":8080/javascript_simple.html";
        String path = "http://" + IP + ":8080/?action=stream";

        // http://192.168.0.56:8080/?action=stream
        webView.loadUrl(path);

        webView.setPadding(0, 0, 0, 0);
    }

    // 웹뷰 화면 사용시 필요
    class WebBridge
    {

        @JavascriptInterface
        public void call_log(final String _message)
        {
            Log.d("hihi", _message);
        }
    }
}
