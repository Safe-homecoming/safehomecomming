package com.example.safehomecoming;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Activity_CCTV extends AppCompatActivity
{

    String IP = "192.168.0.46";
    WebView webView;

    private String TAG = "Activity_CCTV";
    private TextView criminal_count // 감지 횟수
            ,       criminal_date   // 감지한 날짜 / 시간
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__cctv);

        webView = findViewById(R.id.webView_cctv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebBridge(), "java");

        criminal_count = findViewById(R.id.criminal_count);
        criminal_date = findViewById(R.id.criminal_date);

        // set the scale 웹뷰 사이즈 조절
//        webView.setInitialScale(320); // 330 == 330%

//        String path = "http://" + IP + ":8080/javascript_simple.html";
        String path = "http://" + IP + ":8080/?action=stream";

        // http://192.168.0.56:8080/?action=stream
        webView.loadUrl(path);

        webView.setPadding(0, 0, 0, 0);

        getCriminalRecordInfo();
    }

    String split[];

    // todo: 범죄자 감지기록 불러오기
    void getCriminalRecordInfo()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/getCriminalRecord.php", // todo: php 파일 주소
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "onResponse: " + response);


                        split = response.trim().split("//");
                        Log.e(TAG, "onResponse: split[0]" + split[0] );
                        Log.e(TAG, "onResponse: split[1]" + split[1] );

                        criminal_count.setText(split[0] + "회");
                        criminal_date.setText(split[1]);

                        // criminal_count   criminal_date
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("VolleyError", "에러: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        // requestQueue로 로그인 결과값 요청을 시작한다.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_CCTV.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    // 웹뷰 화면 사용시 필요
    class WebBridge
    {
        @JavascriptInterface
        public void call_log(final String _message)
        {
            Log.e("hihi", _message);
        }
    }
}
