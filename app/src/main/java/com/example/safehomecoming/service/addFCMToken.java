package com.example.safehomecoming.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.service.autofill.UserData;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safehomecoming.citizen.Activity_Wait_for_request_guard_response;
import com.example.safehomecoming.citizen.Activity_fail_request_guard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_CITIZEN_PATH;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_DESTINATION;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_GUARD_GENDER;

public class addFCMToken
{
    private String TAG = "addFCMToken";

    String token;

    public addFCMToken()
    {
    }

    // todo: FCM 디바이스 토큰 추출 후 member 테이블 FCM_Token 컬럼에 업데이트 하기.
    public void addFCM_Token(final String userId, final Context context) // userId Token
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task)
                    {
                        if (!task.isSuccessful())
                        {
                            Log.e(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        Log.e(TAG, "onComplete: token: " + token);

                        StringRequest stringRequest
                                = new StringRequest(Request.Method.POST,
                                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/updateFCM_Token.php", // todo: php 파일 주소
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        Log.e(TAG, "onResponse: " + response.trim());

                                        // todo: 토큰 업데이트 성공
                                        if (response.trim().equals("add_token_success"))
                                        {
                                            Log.e(TAG, "onResponse: 토큰 업데이트 성공");
                                        }

                                        // todo: 토큰 업데이트 실패
                                        else
                                        {
                                            Log.e(TAG, "onResponse: 토큰 업데이트 실패. php에서 문제 발생");
                                        }
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

//                                params.put("Authorization", userId);
                                params.put("userId", userId);
                                params.put("token", token);

                                /* Log.e(TAG, "getParams: userId: " + userId);
                                Log.e(TAG, "getParams: token: " + token);*/

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });

    } // 토큰 추가 끝

    // todo: FCM 디바이스 토큰 추출 후 member 테이블 FCM_Token 컬럼에 업데이트 하기.
    public void sendFCM_Message(final String receiveUserToken, final String Title, final String Body, final Context context) // userId Token
    {

//        sendPostToFCM();

/*        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken("기기토큰정보")
                .setNotification(notification)
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("fcmmsg")).send(message );
        } catch (FirebaseMessagingException e) {
            logger.error("Firebase FirebaseMessagingException" + e.getMessage());
            e.printStackTrace();
        }*/

        // 이 등록 토큰은 클라이언트 FCM SDK에서 제공됩니다.
        // This registration token comes from the client FCM SDKs.
//        String registrationToken = "YOUR_REGISTRATION_TOKEN";

        // 메시지 페이로드 정의에 대한 설명서를 참조하십시오.
        // See documentation on defining a message payload.
//        Message message = Message.builder()
//                .putData("score", "850")
//                .putData("time", "2:45")
//                .setToken(registrationToken)
//                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
//        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);


/*        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send", // todo: php 파일 주소
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "onResponse: " + response.trim());

                        // todo: 토큰 업데이트 성공
                        if (response.trim().equals("add_token_success"))
                        {
                            Log.e(TAG, "onResponse: 토큰 업데이트 성공");
                        }

                        // todo: 토큰 업데이트 실패
                        else
                        {
                            Log.e(TAG, "onResponse: 토큰 업데이트 실패. php에서 문제 발생");
                        }
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

            // 파라미터
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();


                JSONObject object = new JSONObject();
                JSONObject notification = new JSONObject(); // JSON 오브젝트의 body 부분
                String json = null;

                try
                {
                    object.put("to", receiveUserToken);

                    object.put("notification", notification.put("body", Body));
                    notification.put("title", Title);

                    json = object.toString();

                    Log.e("json", "생성한 json : " + json);


                    params.put("bodyJson", json);


                } catch (JSONException e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "jsonObjectTest: e: " + e);
                }

//                Log.e(TAG, "getParams: userId: " + userId);
//                Log.e(TAG, "getParams: token: " + token);

                return params;
            }

            // 헤더
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??
                Map params = new HashMap();
                params.put("Authorization", "key=AAAA7pJGgbg:APA91bGoKVGCmJDJ1gPTVZ0ydjnCgpgt1v1NdIk3MalCeiv69VdbWRBx-Q7N6vAYob2WSmC6fkecJ3-SCeq5QqtVjVW7_ghsi8kQi2kRkNSTguJYzdNY6gaN8wDJsrRk5pvO4MaGkJfo");
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);*/
    }

    // key=AAAA7pJGgbg:APA91bGoKVGCmJDJ1gPTVZ0ydjnCgpgt1v1NdIk3MalCeiv69VdbWRBx-Q7N6vAYob2WSmC6fkecJ3-SCeq5QqtVjVW7_ghsi8kQi2kRkNSTguJYzdNY6gaN8wDJsrRk5pvO4MaGkJfo
    //    String Token = "eBo4JYXx1QY:APA91bHYSZz02itI_17dBAVMs7Ul1ib-pEfs5pSIrPfoNveDG8OUaFd81bVLXeuNdqbTR6vZUuQe4apHzaXwuvIYp4Lh0W2CDUHLydk6UhT9zaNj0MJYC6AI4oQ5AkUKgll1E5yi9CiN";
    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAA7pJGgbg:APA91bGoKVGCmJDJ1gPTVZ0ydjnCgpgt1v1NdIk3MalCeiv69VdbWRBx-Q7N6vAYob2WSmC6fkecJ3-SCeq5QqtVjVW7_ghsi8kQi2kRkNSTguJYzdNY6gaN8wDJsrRk5pvO4MaGkJfo";

    public void sendPostToFCM(final String token, final String title, final String message)
    {
        try
        {
            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", message);
            notification.put("title", title);
            root.put("notification", notification);
            root.put("to", token);
            // FMC 메시지 생성 end

            Log.e(TAG, "sendPostToFCM: notification: " + notification );

            URL Url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
//            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();

            Log.e(TAG, "run: 전송완료");
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "run: e: " + e);
        }

    }

    public void jsonObjectTest()
    {
        JSONObject jsonObject = new JSONObject(); //head오브젝트와 body오브젝트를 담을 JSON오브젝트

        JSONObject head = new JSONObject(); //JSON 오브젝트의 head 부분
        JSONObject body = new JSONObject(); //JSON 오브젝트의 body 부분
        JSONObject notification = new JSONObject(); //JSON 오브젝트의 body 부분
        JSONObject data = new JSONObject(); //JSON 오브젝트의 body 부분

        try
        {
            // key=AAAA7pJGgbg:APA91bGoKVGCmJDJ1gPTVZ0ydjnCgpgt1v1NdIk3MalCeiv69VdbWRBx-Q7N6vAYob2WSmC6fkecJ3-SCeq5QqtVjVW7_ghsi8kQi2kRkNSTguJYzdNY6gaN8wDJsrRk5pvO4MaGkJfo
//            head.put("Authorization", "key=");
//            head.put("Content-Type", "application/json");

//            jsonObject.put("Header", head); //body 오브젝트 추가

//            body.put("priority", "high");
//            body.put("to", "token");
//            body.put("notification", notification.put("body", "Background Message"));
//            notification.put("title", "BG Title");
//
//            jsonObject.put("", body); //body 오브젝트 추가


            ////

            // Create a new instance of a JSONObject
            final JSONObject object = new JSONObject();

            // With put you can add a name/value pair to the JSONObject
            object.put("to", "token");
            object.put("notification", notification.put("body", "Background Message"));
            notification.put("title", "BG Title");
//            object.put("null_value", JSONObject.NULL);

            // Calling toString() on the JSONObject returns the JSON in string format.
            final String json = object.toString();
            Log.e("json", "생성한 json : " + json); //log로 JSON오브젝트가 잘생성되었는지 확인

        } catch (JSONException e)
        {
            e.printStackTrace();
            Log.e(TAG, "jsonObjectTest: e: " + e);
        }
//        Log.e("json", "생성한 json : " + jsonObject.toString()); //log로 JSON오브젝트가 잘생성되었는지 확인

    }

}
