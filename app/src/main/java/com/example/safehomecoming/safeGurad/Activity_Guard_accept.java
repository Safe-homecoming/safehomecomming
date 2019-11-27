package com.example.safehomecoming.safeGurad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.safehomecoming.R;
import com.example.safehomecoming.retrifit_setup.ApiClient;
import com.example.safehomecoming.retrifit_setup.ApiInterface;
import com.example.safehomecoming.retrifit_setup.Resultm;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Guard_accept extends AppCompatActivity implements RecyclerAdapter_Accept_Guard.Guard_viewClickListener {


    //리사이클러뷰 관련
    private RecyclerAdapter_Accept_Guard adapter;
    String[] stId;   // request 인덱스 번호
    String[] stGender; //요청자 성별
    String[] stName; //요청자 이름
    Integer [] stHome_distance; //출발 지점에서 도착지점의 거리 db에서 가져올때  미터 기준으로 가져옴
    Double[] stcLatitude;  //요청자의 위도
    Double[] stcLongitude; //요청자의 경도


    // GPS 현재위치 관련 변수
    String provider;  //현재위치 정보
    double longitude; //위도
    double latitude; //경도
    double altitude ; // 고도
    private String TAG = "Guard_accept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_accept);


        //GPS 권한 체크 하기
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //현재위치 가져오기
        curLocation();

        //당겨서 새로고침
        final SwipeRefreshLayout mSwipRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    curLocation(); //현재 위치
                    init();
                    getData();  // 리사이클러뷰 에 data 넣어주기
                    mSwipRefreshLayout.setRefreshing(false);
                }
            });



        init();  // 리사이클러뷰 셋팅
        getData();  // 리사이클러뷰 에 data 넣어주기


    }


    // 현재 위치 가져오기
    void curLocation(){
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( Activity_Guard_accept.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            //가장최근의 위치정보를 가지고옵니다
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            provider = location.getProvider(); // 위치 정보
            longitude = location.getLongitude();  // 위도
            latitude = location.getLatitude(); // 경도
            altitude = location.getAltitude(); // 고도
            Log.i("Activity_Guard_test","위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);


            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

    }
    //현재 위도 경도 위치 가져오기
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            Log.i("Guard_onLocationChanged","위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도  : " + altitude);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };




    /// 리사이클러뷰 셋팅 메소드
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter_Accept_Guard();
        recyclerView.setAdapter(adapter);

        adapter.setOnMemo_ViewClickListener(this);
    }
    //리사이클러뷰 수락 버튼 누른후
    @Override
    public void onAcceptClicked(View v, int position) {
        Log.i("acceptBtnTesy","수락 버튼 눌렸다잉");
    }

    /// 리사이클러뷰 Data 넣어줌
    private void getData() {


        //현재 위치 받아오기
        final Location crntLocation = new Location("crntlocation");
        crntLocation.setLatitude(latitude);  //경도
        crntLocation.setLongitude(longitude); //위도

        //Log.i("TestLog" ,"   경도 :"+crntLocation.getLatitude()+"   위도"+crntLocation.getLongitude());


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Resultm> call = apiInterface.requestinfo();

        call.enqueue(new Callback<Resultm>() {
            @Override
            public void onResponse(Call<Resultm> call, Response<Resultm> response) {

                //정상 결과
                Resultm result = response.body();
                String result_code = response.body().getResult_code();
                int result_row = response.body().getResult_row();

                // String 배열 초기화
                stId = new String[result_row];  // reqeust 인덱스 번호
                stGender = new String[result_row];  //요청자 성별
                stName = new String[result_row]; // 요청자 이름
                stcLatitude = new Double[result_row]; //요청자의 위도
                stcLongitude = new Double[result_row]; //요청자의 경도
                stHome_distance = new Integer[result_row];// 출발에서 도착까지의 미터 거리

                if (response.body() != null) {
                    // if (result.getResult().equals("ok")) {
                    if (result_code.equals("100")) {
                        for(int i=0; i< result.getItems().size(); i++) {
                            // 서버에서 보내준 json 값들 String 배열에 다시 넣어주기
                            stId[i] = result.getItems().get(i).getIdx();  // reqeust 인덱스 번호
                            stGender[i] = result.getItems().get(i).getGender(); //요청자 성별
                            stName[i] = result.getItems().get(i).getName(); // 요청자 이름
                            stHome_distance[i] = Integer.parseInt(result.getItems().get(i).getHomecoming_distance()); // 출발에서 도착까지의 미터 거리

                            stcLatitude[i] = Double.parseDouble(result.getItems().get(i).getCur_latitude()); //요청자의 위도
                            stcLongitude[i] = Double.parseDouble(result.getItems().get(i).getCur_longitude()); //요청자의 경도
                        }

                        // 배열을 list에 담기
                        List<String> listId = Arrays.asList(stId);
                        List<String> listgender = Arrays.asList(stGender);
                        List<String> listname = Arrays.asList(stName);
                        List<Integer> listdistance = Arrays.asList(stHome_distance);
                        List<Double> listlatitude = Arrays.asList(stcLatitude);
                        List<Double> listlongitude = Arrays.asList(stcLongitude);

                        // 요청자 성별 icon 이미지 배열
                        List<Integer> listResId = Arrays.asList(
                                R.drawable.ic_person_female,
                                R.drawable.ic_person_male);

                        for (int i = 0; i < listId.size(); i++) {
                            // 각 List의 값들을 Accept_Data 객체에 set 해줍니다.
                            Accept_Data data = new Accept_Data();
                            data.setLeftKm(listdistance.get(i));// 출발에서 도착까지의 미터 거리
                            data.setName(listname.get(i));// 요청자 이름

                            // 요청자의 현재 위치
                            Location newLocation=new Location("newlocation");
                            newLocation.setLatitude(listlatitude.get(i));
                            newLocation.setLongitude(listlongitude.get(i));

                            // 안심이와 요청자 간의 거리 계산
                            float distance = crntLocation.distanceTo(newLocation);///1000; //in km
                           // Log.i("Test_Log","   안심과 요청자 간 거리"+distance);

                            // 안심이와 요청자 간의 거리
                            data.setWorkKm((int)distance);

                            // 요청자 성별 이미지로 구분
                            if(listgender.get(i).equals("female")){
                                data.setResId(listResId.get(0));
                            }else{
                                data.setResId(listResId.get(1));
                            }

                            // 각 값이 들어간 data를 adapter에 추가합니다.
                            adapter.addItem(data);
                        }
                        // adapter의 값이 변경되었다는 것을 알려줍니다.
                        adapter.notifyDataSetChanged();


                    } else if (result_code.equals("200")) {
                        //실패
                        Toast.makeText(getApplicationContext(), "불러오기 실패하였습니다. 확인 부탁드립니다.", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Resultm> call, Throwable t) {
                //네트워크 문제
                Toast.makeText(getApplicationContext(), "서비스 연결이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                Log.e("메모 에러 발생 Log ", t.getMessage());
            }
        });

    }


}
