package com.example.safehomecoming.citizen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.safehomecoming.Activity_CCTV;
import com.example.safehomecoming.Emergency_Mode;
import com.example.safehomecoming.R;
import com.example.safehomecoming.service.MyFirebaseMessagingService;
import com.example.safehomecoming.service.addFCMToken;
import com.example.safehomecoming.service.MyFirebaseInstanceIDService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.safehomecoming.Activity_Login.GET_TOKEN;
import static com.example.safehomecoming.Activity_Login.GET_USER_ID;

public class Activity_Main_Citizen extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private String TAG = "Activity_Main_Citizen";

    // 구글 맵 관련 변수 모음
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker = null;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location mCurrentLocatiion;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;

    LocationRequest locationRequest = new LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
    // 구글 맵 관련 변수 모음 끝

    private TextView button_request_safe_guard  //
            , button_my_guard_info        //
            ;

    private ImageView
            button_nav      // 네비게이션 on / off 버튼
            , helper_icon   // 안심이 정보 버튼
            ;

    private LinearLayout
            nav_area            // 네비게이션 영역
            , nav_mypage      // 네이게이션 버튼_마이페이지
            , nav_boundary    // 네이게이션 버튼_경계모드
            , nav_my_child    // 네이게이션 버튼_미아찾기
            , nav_cctv        // 네이게이션 버튼_CCTV
            ;

    boolean navIsOpen = false;

    public static String GET_CURRENT_CITIZEN_PATH, GET_LATITUDE, GET_LONGITUDE, GET_CURRENT_FEATURE_NAME;

    // 다른 액티비티에서 액티비티 해당 액티비티의 종료 권한을 준다
    public static Activity Activity_Main_Citizen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_citizen);

        Log.e(TAG, "onCreate: ");

        Activity_Main_Citizen = Activity_Main_Citizen.this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
        startService(intent);

        Intent intent1 = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent1);

        // todo: 유저의 FCM 토큰 추가하기
        addFCMToken addFCMToken = new addFCMToken();
        addFCMToken.addFCM_Token(GET_USER_ID, Activity_Main_Citizen.this);

        // todo: View Find
        mActivity = this;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_citizen);
        mapFragment.getMapAsync(this);

        button_my_guard_info = findViewById(R.id.button_my_guard_info);
        button_request_safe_guard = findViewById(R.id.button_request_safe_guard);

        // 네비게이션
        button_nav = findViewById(R.id.button_nav);
        nav_area = findViewById(R.id.nav_area);
        nav_mypage = findViewById(R.id.nav_mypage);
        nav_boundary = findViewById(R.id.nav_boundary);
        nav_my_child = findViewById(R.id.nav_my_child);
        nav_cctv = findViewById(R.id.nav_cctv_c);

        helper_icon = findViewById(R.id.helper_icon);
        // View Find 끝

        // todo: 네이게이션 버튼 이벤트 모음
        navButtonSet();

        // 네비게이션 세팅

        // todo: 네비게이션 버튼 모양 세팅
        if (navIsOpen)
        {
            // button_nav.setImageResource(R.drawable.ic_nav_cancel);
            // button_nav.setBackground(getDrawable(R.drawable.ic_nav_cancel));
            button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_cancel));
            Log.e(TAG, "onCreate: navIsOpen: " + navIsOpen);
            navIsOpen = false;
        }

        else
        {
//            button_nav.setImageResource(R.drawable.ic_nav_open);
            button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_open));
//            button_nav.setBackground(getDrawable(R.drawable.ic_nav_open));
            Log.e(TAG, "onCreate: navIsOpen: " + navIsOpen);
            navIsOpen = true;
        }

        // todo: 네이게이션 버튼 클릭
        button_nav.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                if (navIsOpen)
                {
                    button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_open));
                    Log.e(TAG, "onCreate: button_nav navIsOpen: " + navIsOpen);
                    nav_area.setVisibility(View.GONE);
                    navIsOpen = false;
                } else
                {
                    button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_cancel));
                    Log.e(TAG, "onCreate: button_nav navIsOpen: " + navIsOpen);
                    nav_area.setVisibility(View.VISIBLE);
                    navIsOpen = true;
                }
            }
        });

        Log.e(TAG, "navButtonSet: ");
        nav_cctv.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                Log.e(TAG, "navButtonSet: nav_cctv");
                Intent intent = new Intent(Activity_Main_Citizen.this, Activity_CCTV.class);
                startActivity(intent);
            }
        });

        // 안심 귀가요청 화면으로 이동
        button_request_safe_guard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_Main_Citizen.this, Activity_safeGuard_Call_Request.class);
                startActivity(intent);
            }
        });

        //경계 모드로
        nav_boundary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Main_Citizen.this, Emergency_Mode.class);
                startActivity(intent);
            }
        });
        // FCM 전송 테스트
        button_my_guard_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                final String token = "dk0bNURpl28:APA91bGZgxStbI_edfXvvRqSvy1UD8UJcTV-8dXF_76DLFSzCRa4QxAJWIbiF6BcHsqj7FN96CQWW5ajKP1dveSDGghDfIuR85J3H8bQprIKeTm9ntDgvvT3oGFQbmYd8wbn6iVBx2nO";
                //        addFCMToken.sendPostToFCM(token, "제목", "됬다!!! 하하!");
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Log.e(TAG, "run: FMC 메시지 생성 start");
                            // FMC 메시지 생성 start
                            JSONObject root = new JSONObject();
                            JSONObject notification = new JSONObject();
                            notification.put("body", "됬다!!! 하하!");
                            notification.put("title", "제목");
                            root.put("notification", notification);
                            root.put("to", token);
                            // FMC 메시지 생성 end
                            Log.e(TAG, "sendPostToFCM: notification: " + notification);

                            URL Url = new URL("https://fcm.googleapis.com/fcm/send");
//                            URL Url = new URL("https://fcm.googleapis.com/v1/projects/safehomecomming/messages:send");
                            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.addRequestProperty("Authorization", "key=" + "AAAA7pJGgbg:APA91bGoKVGCmJDJ1gPTVZ0ydjnCgpgt1v1NdIk3MalCeiv69VdbWRBx-Q7N6vAYob2WSmC6fkecJ3-SCeq5QqtVjVW7_ghsi8kQi2kRkNSTguJYzdNY6gaN8wDJsrRk5pvO4MaGkJfo");
                            conn.setRequestProperty("Accept", "application/json");
                            conn.setRequestProperty("Content-type", "application/json");
                            OutputStream os = conn.getOutputStream();
                            os.write(root.toString().getBytes("utf-8"));
                            os.flush();
                            conn.getResponseCode();

                            Log.e(TAG, "run: conn: " + conn.toString() );
                            Log.e(TAG, "run: conn.getResponseCode(): " + conn.getResponseCode() );

                            Log.e(TAG, "run: 전송완료");
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.e(TAG, "run: e: " + e);
                        }
                    }
                }).start();
            }
        });

        // 미아 찾기
        nav_my_child.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getStrayChild();
            }
        });

        // todo: 매칭 여부 확인하기
        getMatchResult(GET_USER_ID);
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
//            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
//            return "주소 미발견";
            return " ";
        } else
        {
            Address address = addresses.get(0);

            return address.getAddressLine(0).toString();
        }
    }

    // todo: 네비게이션 버튼 이벤트 모음
    private void navButtonSet()
    {
//        // 네비게이션 세팅
//
//        // todo: 네비게이션 버튼 모양 세팅
//        if (navIsOpen)
//        {
////            button_nav.setImageResource(R.drawable.ic_nav_cancel);
//            button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_cancel));
////            button_nav.setBackground(getDrawable(R.drawable.ic_nav_cancel));
//            Log.e(TAG, "onCreate: navIsOpen: " + navIsOpen );
//            navIsOpen = false;
//        }
//
//        else
//        {
////            button_nav.setImageResource(R.drawable.ic_nav_open);
//            button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_open));
////            button_nav.setBackground(getDrawable(R.drawable.ic_nav_open));
//            Log.e(TAG, "onCreate: navIsOpen: " + navIsOpen );
//            navIsOpen = true;
//        }
//
//        // todo: 네이게이션 버튼 클릭
//        button_nav.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (navIsOpen)
//                {
//                    button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_open));
//                    Log.e(TAG, "onCreate: button_nav navIsOpen: " + navIsOpen );
//                    nav_area.setVisibility(View.GONE);
//                    navIsOpen = false;
//                }
//
//                else
//                {
//                    button_nav.setImageDrawable(getResources().getDrawable(R.drawable.ic_nav_cancel));
//                    Log.e(TAG, "onCreate: button_nav navIsOpen: " + navIsOpen );
//                    nav_area.setVisibility(View.VISIBLE);
//                    navIsOpen = true;
//                }
//            }
//        });
//
//        Log.e(TAG, "navButtonSet: " );
//        nav_cctv.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Log.e(TAG, "navButtonSet: nav_cctv" );
//                Intent intent = new Intent(Activity_Main_Citizen.this, Activity_CCTV.class);
//                startActivity(intent);
//            }
//        });
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
                            // 매칭 수락한 유저가 있다면 화면 오른 쪽 위에 안심이가 오는 중이라고 알려주기
                            // todo: 안심이 화면으로 이동

                            helper_icon.setVisibility(View.VISIBLE);

                            helper_icon.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent2 = new Intent(Activity_Main_Citizen.this, Activity_success_guard_request.class);
                                    startActivity(intent2);
                                }
                            });

                        }

                        else
                        {
                            // 아직 매칭 안 됨
                            Log.e(TAG, "onResponse: 아직 안심이의 응답 없음: " );
                            helper_icon.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Main_Citizen.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    String StrayChildLocation[];
    // todo: 미아 위치 조회하기
    private void getStrayChild()
    {
        Log.e(TAG, "getStrayChild(): 미아 위치 조회하기");

        StringRequest stringRequest
                = new StringRequest(Request.Method.POST,
                "http://ec2-13-125-121-5.ap-northeast-2.compute.amazonaws.com/getStrayChild.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e(TAG, "updateMatchFail onResponse: " + response.trim());

                        StrayChildLocation = response.trim().split(", ");

                        Log.e(TAG, "onResponse: 위도: " + StrayChildLocation[0] );
                        Log.e(TAG, "onResponse: 경도: " + StrayChildLocation[1] );
                        Log.e(TAG, "onResponse: 이름: " + StrayChildLocation[2] );

                        // 주소 추출
                        String Location = getCurrentAddress(Double.parseDouble(StrayChildLocation[0]), Double.parseDouble(StrayChildLocation[1]));

                        Log.e(TAG, "onResponse: 주소: " + Location );

                        Toast.makeText(mActivity,
                                   "보호중인 아이의 정보 \n" +
                                           StrayChildLocation[2] +" \n" +
                                        "위도: " + StrayChildLocation[0] + "\n" +
                                        "경도: " + StrayChildLocation[1] + "\n" +
                                         Location,
                                Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Main_Citizen.this);

        // stringRequest메소드에 기록한 내용들로 requestQueue를 시작한다.
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    // todo: 아래부터 전부 구글 지도 관련 메소드 (코드 분석 필요합니다)
    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(19));

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
        {
            @Override
            public boolean onMyLocationButtonClick()
            {
                Log.e(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                Log.e(TAG, "onMapClick :");
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener()
        {
            @Override
            public void onCameraMoveStarted(int i)
            {
                if (mMoveMapByUser == true && mRequestingLocationUpdates)
                {
                    Log.e(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }
                mMoveMapByUser = true;
            }
        });

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {

            }
        });
    }

    private void startLocationUpdates()
    {
        if (!checkLocationServicesStatus())
        {
            Log.e(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.e(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void stopLocationUpdates()
    {
        Log.e(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (mGoogleApiClient.isConnected())
        {
            Log.e(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        currentPosition
                = new LatLng(location.getLatitude(), location.getLongitude());

//        Log.d(TAG, "onLocationChanged : ");

//        Log.e(TAG, "onLocationChanged: currentPosition: " + currentPosition);

        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

//        Log.e(TAG, "onLocationChanged: markerSnippet: " + markerSnippet);

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);

        mCurrentLocatiion = location;
    }

    @Override
    protected void onStart()
    {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() == false)
        {

            Log.e(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }
        super.onStart();
    }


    @Override
    protected void onStop()
    {
        if (mRequestingLocationUpdates)
        {
            Log.e(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if (mGoogleApiClient.isConnected())
        {
            Log.e(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if (mRequestingLocationUpdates == false)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else
                {
                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else
            {

                Log.e(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.e(TAG, "onConnectionFailed");
        setDefaultLocation();
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        Log.e(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(LatLng latlng)
    {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try
        {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
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
//            Log.e(TAG, "getCurrentAddress: address: " + address.toString());

//            Log.e(TAG, "getCurrentAddress: address.getAddressLine: " + address.getAddressLine(0));

//            Log.e(TAG, "getCurrentAddress: address getAdminArea 주소: " + address.getAdminArea());
//            Log.e(TAG, "getCurrentAddress: address thoroughfare 동: " + address.getThoroughfare());
//            Log.e(TAG, "getCurrentAddress: address getFeatureName 번지: " + address.getFeatureName());

//            Log.e(TAG, "getCurrentAddress: address latitude 위도: " + address.getLatitude());
//            Log.e(TAG, "getCurrentAddress: address longitude 경도: " + address.getLongitude());

            // 현재 주소
            GET_CURRENT_CITIZEN_PATH = address.getAdminArea() + " " + address.getThoroughfare();

            //
            GET_CURRENT_FEATURE_NAME = address.getFeatureName();

            //
            GET_LATITUDE = String.valueOf(address.getLatitude());

            //
            GET_LONGITUDE = String.valueOf(address.getLongitude());

            // latitude
            // longitude

            return address.getAddressLine(0).toString();
        }
    }


    public boolean checkLocationServicesStatus()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet)
    {

        mMoveMapByUser = false;


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mGoogleMap.addMarker(markerOptions);


        if (mMoveMapByAPI)
        {

            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude());
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }


    public void setDefaultLocation()
    {

        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions()
    {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale)
        {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED)
        {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if (mGoogleApiClient.isConnected() == false)
            {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0)
        {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted)
            {


                if (mGoogleApiClient.isConnected() == false)
                {
                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }


            } else
            {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Main_Citizen.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Main_Citizen.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Main_Citizen.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus())
                {
                    if (checkLocationServicesStatus())
                    {
                        Log.e(TAG, "onActivityResult : 퍼미션 가지고 있음");

                        if (mGoogleApiClient.isConnected() == false)
                        {
                            Log.e(TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
    }
}
