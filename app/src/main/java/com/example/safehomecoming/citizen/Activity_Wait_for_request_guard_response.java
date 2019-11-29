package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safehomecoming.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.safehomecoming.Activity_Login.GET_USER_ID;
import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_CITIZEN_PATH;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_DESTINATION;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_GUARD_GENDER;

public class Activity_Wait_for_request_guard_response extends AppCompatActivity
{
    private String TAG = "Activity_Wait_for_request_guard_response";

    private TextView
            button_cancel_request_safe_guard // 요청 취소 버튼
            ;

    long elapsed;

    final static long INTERVAL = 1000; // 1초 에 1씩 증가
    static long TIMEOUT = 11000; // 종료 할 시간 설정 (10000 = 10초)

    private Timer timer;

    // 출발 지점 좌표
    private String[] startLatFilter;
    private String[] endLatFilter;

    private String timerType;
    public static String GET_FAIL_MESSAGE;

    // todo: 타이머 스레드 메소드
    public void tempTask()
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                // 1 초 씩 증가
                elapsed += INTERVAL;
                if (elapsed >= TIMEOUT)
                {
                    this.cancel(); // 설정한 시간까지 흐르면 타이머 중단
                    return;
                }

                // 현재 초를 displayText 메소드로 전달
                displayText(String.valueOf(elapsed / 1000));
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(task, INTERVAL, INTERVAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_request_guard_response);

        // View Find
        button_cancel_request_safe_guard = findViewById(R.id.button_cancel_request_safe_guard);

//        Log.e(TAG, "onCreate: 출발 지점 주소: " + GET_CURRENT_CITIZEN_PATH);
        Log.e(TAG, "onCreate: getCurrentAddressName: " + getCurrentAddressName(GET_CURRENT_CITIZEN_PATH));

        // startLatFilter endLatFilter
        startLatFilter = getCurrentAddressName(GET_CURRENT_CITIZEN_PATH).split("-");

        Log.e(TAG, "onCreate: 출발 지점: " + GET_CURRENT_CITIZEN_PATH);
        Log.e(TAG, "onCreate: startLatFilter: 위도: " + startLatFilter[0]);
        Log.e(TAG, "onCreate: startLatFilter: 경도: " + startLatFilter[1]);

        // 도착지점 null 예외처리
        if (TextUtils.isEmpty(GET_SELECT_DESTINATION))
        {
        } else
        {
            endLatFilter = getCurrentAddressName(GET_SELECT_DESTINATION).split("-");

            Log.e(TAG, "onCreate: 도착 지점: " + GET_SELECT_DESTINATION);
            Log.e(TAG, "onCreate: 도착 지점 위도: " + endLatFilter[0]);
            Log.e(TAG, "onCreate: 도착 지점 경도: " + endLatFilter[1]);
        }

        Log.e(TAG, "onCheckedChanged: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER);
//        Log.e(TAG, "onCheckedChanged: 출발, 도착 두 지점 사이의 거리 반환 됨 _ 미터(m): " +
//                DistanceByDegree(
//                        startLatFilter[0],
//                        startLatFilter[1],
//                        endLatFilter[0],
//                        endLatFilter[1]));

        // 안심이의 좌표 정보 필요
//        Log.e(TAG, "onCheckedChanged: 출발, 안심이 두 지점 사이의 거리: " + GET_SELECT_DESTINATION);

        // todo: 요청 취소 버튼
        button_cancel_request_safe_guard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        // 타이머 사용 용도 설정
        timerType = "request"; // 'request' == 5 초 후에 안심이 조회 시작. 로딩 느낌 나게끔 만들기
        TIMEOUT = 6000; // 타이머 6초로 설정
        tempTask(); // 타이머 시작
    }

    // todo: 매칭 시작 (request 테이블에 값 추가하기)
    private void addRequestInfo(final String start_address, // 출발 지점 주소
                                final String start_lat,
                                final String start_long,    // 출발 지점 위도, 경도
                                final double distance,      // 출발 지점과 도착 지점 사이의 거리
                                final String end_address,   // 도착 지점 주소
                                final String end_lat,
                                final String end_long,      // 도착 지점 위도 경도
                                final String gender,        // 시민이 요청한 안심이의 성별
                                final String userId         // 시민의 Id (index)
    )
    {
        Log.e(TAG, "addRequestInfo(): 매칭 시작");

        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/addRequestInfo.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "onResponse: " + response.trim());

                        if (response.trim().equals("matchStart"))
                        {
                            timerType = "waitForHelperRequest"; // 'waitForHelperRequest' == 00 초간 안심이가 응답 없으면 매칭 실패로 간주함
                            elapsed = 0;
                            TIMEOUT = 31000; // 30초

                            tempTask();
                        } else
                        {
                            Log.e(TAG, "onResponse: php 측에서 문제 발생");
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

                params.put("start_address", start_address);
                params.put("start_lat", start_lat);
                params.put("start_long", start_long);
                params.put("distance", String.valueOf(distance));

                params.put("end_address", end_address);
                params.put("end_lat", end_lat);
                params.put("end_long", end_long);
                params.put("gender", gender);

                params.put("userId", userId);

                return params;
            }
        };

        // requestQueue로 로그인 결과값 요청을 시작한다.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Wait_for_request_guard_response.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    // todo: 설정한 조건으로 안심이 조회하기
    private void getHelperInfo(
            final String userLatitude,  // 출발 지점 위도
            final String userLongitude, // 출발 지점 경도
            final String reqGender      // 요청할 안심이의 성별
    )
    {
        Log.e(TAG, "getHelperInfo(): 활동 중 / 조건에 맞는 안심이 조회");

        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/searchHelper.php", // todo: php 파일 주소
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "onResponse: " + response.trim());

                        // todo: 조회 실패
                        if (response.trim().equals("fail"))
                        {
                            Log.e(TAG, "onResponse: 조회 실패. 출발지 주변에 활동중인 안심이가 없어요 :(");
                            GET_FAIL_MESSAGE = "출발 지점 주변에\n\n활동중인 안심이가 없어요 :(";
                            Log.e(TAG, "onResponse: 조회 실패. 내 주변에 활동중인 안심이, 혹은 조건에 맞는 안심이가 없음");
                            Intent intent = new Intent(
                                    Activity_Wait_for_request_guard_response.this,
                                    Activity_fail_request_guard.class);
                            startActivity(intent);
                            finish();
                        }

                        // todo: 조회 성공
                        else if (response.trim().equals("success"))
                        {
                            Log.e(TAG, "onResponse: 조회 성공. 00초간 안심이의 응답을 대기합니다");

                            // todo: 매칭 시작하기
                            addRequestInfo(GET_CURRENT_CITIZEN_PATH, // 출발 지점의 주소
                                    startLatFilter[0],
                                    startLatFilter[1],
                                    DistanceByDegree(startLatFilter[0], startLatFilter[1], endLatFilter[0], endLatFilter[1]), // 출발 지점과 도착 지점 사이의 거리
                                    GET_SELECT_DESTINATION, // 도착 지점의 주소
                                    endLatFilter[0],
                                    endLatFilter[1],
                                    GET_SELECT_GUARD_GENDER,
                                    GET_USER_ID);
                        }

                        // todo: 조회됨 && 일치하는 성별 없음
                        else if (response.trim().equals("noMatchingGender"))
                        {
                            // ""
                            Log.e(TAG, "onResponse: 요청한 성별의 안심이가 없지만, 다른 성별로 안심이를 요청할 수 있음");

                            // 경고 다이얼로그: 일치하는 성별의 안심이가 없음
                            AlertDialog.Builder photo_upload_Dialog
                                    = new AlertDialog.Builder(Activity_Wait_for_request_guard_response.this);

                            // 다이얼로그 세팅
                            photo_upload_Dialog.setTitle("요청한 안심이가 없어요");
                            photo_upload_Dialog
                                    .setMessage("\n다른 성별의 안심이로 요청은 가능합니다.\n\n계속 하시겠습니까?")
                                    .setCancelable(false) // 다이얼로그 실행되면 뒤로가기 비활성화
                                    .setPositiveButton("네",
                                            new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    // todo: 매칭 시작하기
                                                    addRequestInfo(GET_CURRENT_CITIZEN_PATH, // 출발 지점의 주소
                                                            startLatFilter[0],
                                                            startLatFilter[1],
                                                            DistanceByDegree(startLatFilter[0], startLatFilter[1], endLatFilter[0], endLatFilter[1]), // 출발 지점과 도착 지점 사이의 거리
                                                            GET_SELECT_DESTINATION, // 도착 지점의 주소
                                                            endLatFilter[0],
                                                            endLatFilter[1],
                                                            "anyone",
                                                            GET_USER_ID);
                                                }
                                            })
                                    .setNegativeButton("아니요",
                                            new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    dialog.cancel(); // 다이얼로그 닫기

                                                    GET_FAIL_MESSAGE = "요청하신 안심이가\n\n주변에 없어요 :(";

                                                    // 매칭 실패 처리하기
                                                    Intent intent = new Intent(
                                                            Activity_Wait_for_request_guard_response.this,
                                                            Activity_fail_request_guard.class);
                                                    startActivity(intent);
                                                    finish();
//                                                    updateMatchFail("user1");
                                                }
                                            });

                            final AlertDialog dialog = photo_upload_Dialog.create();

                            dialog.setOnShowListener(new DialogInterface.OnShowListener() // 다이얼로그 색상 설정
                            {
                                @Override
                                public void onShow(DialogInterface arg0)
                                {
                                    // 아니오 버튼의 색상 Color.rgb(247, 202, 201) = 분홍색
                                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);

                                    // 네 버튼의 색상
                                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                                }
                            });
                            dialog.show(); // 다이얼로그 실행
                        }

                        //
                        else
                        {
                            Log.e(TAG, "onResponse: 알 수 없는 결과...");
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

                params.put("userLatitude", userLatitude);
                params.put("userLongitude", userLongitude);
                params.put("reqGender", reqGender);

                return params;
            }
        };

        // requestQueue로 로그인 결과값 요청을 시작한다.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Wait_for_request_guard_response.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    // todo: 매칭 실패 처리하기
    private void updateMatchFail(final String userId)
    {
        Log.e(TAG, "addRequestInfo(): 매칭 실패 처리하기");

        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/updateMatchFail.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "updateMatchFail onResponse: " + response.trim());

                        if (response.trim().equals("failUpdate_success"))
                        {
                            Intent intent = new Intent(
                                    Activity_Wait_for_request_guard_response.this,
                                    Activity_fail_request_guard.class);
                            startActivity(intent);
                            finish();
                        } else
                        {
                            Log.e(TAG, "updateMatchFail onResponse: php 측에서 문제 발생");
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

                params.put("userId", userId);

                return params;
            }
        };

        // requestQueue로 로그인 결과값 요청을 시작한다.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Wait_for_request_guard_response.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    // todo: 매칭 수락한 유저 조회하기
    private void getMatchResult(final String userId)
    {
        Log.e(TAG, "getMatchResult(): 매칭 수락한 유저 조회하기");

        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/correcthelper.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "updateMatchFail onResponse: " + response.trim());

                        if (response.trim().equals("ok")) // 누군가 매칭 되었을 때
                        {
                            // 매칭 성공 화면으로 이동하기
                            // todo: 매칭 성공 액티비티로 이동 (임시)
                            Log.e(TAG, "onResponse: 매칭 성공! 안심이가 응답했습니다 ");

                            Intent intent = new Intent(
                                    Activity_Wait_for_request_guard_response.this,
                                    Activity_success_guard_request.class);

                            startActivity(intent);

                            finish();
                        } else
                        {
                            // 아직 매칭 안 됨
                            Log.e(TAG, "onResponse: 아직 안심이의 응답 없음: ");
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

                params.put("userId", userId);

                return params;
            }
        };

        // requestQueue로 로그인 결과값 요청을 시작한다.
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Wait_for_request_guard_response.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    // todo: 주소를 좌표로 변환
    public String getCurrentAddressName(String addresss)
    {
        // 주소를 좌표로 변환하기
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try
        {
            addresses = geocoder.getFromLocationName(addresss, 1);
        }

        //네트워크 문제
        catch (IOException ioException)
        {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException)
        {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        // 변환 결과
        if (addresses == null || addresses.size() == 0)
        {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else
        {
            Address address = addresses.get(0);

//            Log.e(TAG, "getCurrentAddressName: 주소-> 좌표 변환 결과 반환 됨");
//            Log.e(TAG, "getCurrentAddressName: 위도: " + address.getLatitude());
//            Log.e(TAG, "getCurrentAddressName: 경도: " + address.getLongitude());

            return address.getLatitude() + "-" + address.getLongitude();
        }
    }

    // 두 지점(위도,경도) 사이의 거리
    public double DistanceByDegree(
            String _latitude1,
            String _longitude1,
            String _latitude2,
            String _longitude2)
    {
        double theta, dist;
        theta = Double.parseDouble(_longitude1) - Double.parseDouble(_longitude2);
        dist = Math.sin(DegreeToRadian(Double.parseDouble(_latitude1)))
                *
                Math.sin(DegreeToRadian(Double.parseDouble(_latitude2)))
                + Math.cos(DegreeToRadian(Double.parseDouble(_latitude1)))
                * Math.cos(DegreeToRadian(Double.parseDouble(_latitude2)))
                * Math.cos(DegreeToRadian(theta));

        dist = Math.acos(dist);
        dist = RadianToDegree(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // degree->radian 변환
    public double DegreeToRadian(double degree)
    {
        return degree * Math.PI / 180.0;
    }

    // randian -> degree 변환
    public double RadianToDegree(double radian)
    {
        return radian * 180d / Math.PI;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        timer.cancel(); // 타이머 중단
        finish();
    }

    // 시간 증가 메소드 (초당 1씩 값 증가)
    private void displayText(final String text)
    {
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.e(TAG, "displayText run: timerType: " + timerType);
                Log.e(TAG, "displayText run: text: " + text);

                // 타이머 용도가 request면 로딩 느낌나게 5초 후에 조회 시작
                if (timerType.equals("request"))
                {
                    // todo: 5초 후 안심이 조회 시작
                    if (Integer.parseInt(text) == 5)
                    {
                        // todo: 안심이 조회하기 (활동중이며, 일치하는 성별의 안심이 조회)
                        getHelperInfo(startLatFilter[0], startLatFilter[1], GET_SELECT_GUARD_GENDER);
                        timer.cancel(); // 타이머 중단
                    }
                }

                // 타이머 용도가 'waitForHelperRequest'라면 안심이의 응답 대기하기 (00초간)
                else if (timerType.equals("waitForHelperRequest"))
                {

/*                    if (Integer.parseInt(text) == 3)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    }*/

                    if (Integer.parseInt(text) == 6)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    } else if (Integer.parseInt(text) == 9)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    } else if (Integer.parseInt(text) == 12)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    } else if (Integer.parseInt(text) == 15)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    } else if (Integer.parseInt(text) == 18)
                    {
                        // todo: 매칭 결과 조회하기
                        getMatchResult(GET_USER_ID);
                    }

                    // todo: 20초간 도우미가 응답 없으면 매칭 실패 화면으로 이동
                    else if (Integer.parseInt(text) == 20)
                    {
                        GET_FAIL_MESSAGE = "응답을 받지 못 했습니다\n\n안심이가 모두 바쁜 가봐요 :(";
                        // 매칭 실패 처리하기
                        updateMatchFail(GET_USER_ID);
                    }
                }
            }
        });
    }

    // todo: 위도 경도를 주소로 변환
    public String getCurrentAddress(double lat, double longti)
    {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try
        {
            addresses = geocoder.getFromLocation(
                    lat,
                    longti,
                    1);

        } catch (IOException ioException)
        {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException)
        {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0)
        {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else
        {
            Address address = addresses.get(0);

            return address.getAddressLine(0).toString();
        }
    }

}



/*
try
{
JSONObject jsonObject = new JSONObject(response);

// json에 담긴 문자열 중 success를 추출한다.
String result = jsonObject.getString("success"); // = 1
Log.e(TAG, "onResponse: result: " + result );

} catch (JSONException e)
{
e.printStackTrace();
}
*/