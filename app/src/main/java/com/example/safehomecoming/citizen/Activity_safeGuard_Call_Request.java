package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.safehomecoming.R;

import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_CITIZEN_PATH;
import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_FEATURE_NAME;
import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_LATITUDE;
import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_LONGITUDE;

public class Activity_safeGuard_Call_Request extends AppCompatActivity
{
    private String TAG = "Activity_safeGuard_Call_Request";

    private TextView
            guard_request_address_start     // 출발 지점 출력
            , guard_request_address_end   // 도착 지점 출력
            , button_request_safe_guard   // 버튼 _ 매칭 시작
            ;

    // 안심 귀가 요청할 도우미의 성별 선택 (라디오 그룹)
    private RadioGroup guard_request_radio_group;
    private RadioButton
            guard_request_radio_button_1, guard_request_radio_button_2, guard_request_radio_button_3;

    // 번지수 저장할 배열
    private String[] FeatureNameFilter;

    public static String
            GET_SELECT_GUARD_GENDER  // 성별 선택 결과 저장
            , GET_SELECT_DESTINATION // 도착지점
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_guard_call_request);

        // View Find
        guard_request_address_start = findViewById(R.id.guard_request_address_start);
        guard_request_address_end = findViewById(R.id.guard_request_address_end);

        guard_request_radio_group = findViewById(R.id.guard_request_radio_group);
        button_request_safe_guard = findViewById(R.id.button_request_safe_guard);
        // View Find 끝

        guard_request_address_end.setText("07004, 서울 동작구 사당로13길 31 (사당동, 두산위브 트레지움)");
        GET_SELECT_DESTINATION = "07004, 서울 동작구 사당로13길 31 (사당동, 두산위브 트레지움)";

        Log.e(TAG, "onCreate: 호출 시작할 위치: " + GET_CURRENT_CITIZEN_PATH);
        Log.e(TAG, "onCreate: 호출 시작할 번지: " + GET_CURRENT_FEATURE_NAME);

        Log.e(TAG, "onCreate: 호출 시작할 위도: " + GET_LATITUDE);
        Log.e(TAG, "onCreate: 호출 시작할 경도: " + GET_LONGITUDE);

        // todo: String으로 출력된 번지수를 int로 변환하기
        // String 출력 : ２６６−３１
        // int 출력    : 266-31
        if (TextUtils.isEmpty(GET_CURRENT_FEATURE_NAME))
        {
        } else
        {
            FeatureNameFilter = GET_CURRENT_FEATURE_NAME.split("−");
        }

        if (FeatureNameFilter == null)
        {
        } else if (FeatureNameFilter.length == 2) // 번지수 표시
        {
            int i = Integer.parseInt(FeatureNameFilter[0]);
            int ii = Integer.parseInt(FeatureNameFilter[1]);

            Log.e(TAG, "onCreate: FeatureNameFilter[0]: " + i);
            Log.e(TAG, "onCreate: FeatureNameFilter[1]: " + ii);

            GET_CURRENT_CITIZEN_PATH = GET_CURRENT_CITIZEN_PATH + " " + i + "-" + ii;

            guard_request_address_start.setText(GET_CURRENT_CITIZEN_PATH);
        } else if (FeatureNameFilter.length == 1)
        {
            int i = Integer.parseInt(FeatureNameFilter[0]);

            Log.e(TAG, "onCreate: FeatureNameFilter[0]: " + i);

            GET_CURRENT_CITIZEN_PATH = GET_CURRENT_CITIZEN_PATH + " " + i;

            guard_request_address_start.setText(GET_CURRENT_CITIZEN_PATH);
        }
        // 끝 _ String 으로 출력된 번지수를 int로 변환하기

        // todo: 시작 지점 다시 설정하기
        guard_request_address_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_safeGuard_Call_Request.this, Activity_Select_Address.class);
                startActivityForResult(intent, 1000);
            }
        });

        // todo: 도착 지점 다시 설정하기
        guard_request_address_end.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(Activity_safeGuard_Call_Request.this, Activity_DaumWebView.class);
//                startActivity(intent);
//                startActivityForResult(intent, 2000);

                Intent i = new Intent(Activity_safeGuard_Call_Request.this, Activity_Select_Address.class);
                startActivityForResult(i, 2000);

            }
        });

        // todo: 성별 선택 (라디오 그룹)
        GET_SELECT_GUARD_GENDER = "anyone"; // 기본 선택
        Log.e(TAG, "onCheckedChanged: 출발 지점: " + GET_CURRENT_CITIZEN_PATH);
        Log.e(TAG, "onCheckedChanged: 도착 지점: " + GET_SELECT_DESTINATION);
        Log.e(TAG, "onCheckedChanged: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER);
        RadioGroup.OnCheckedChangeListener RadioGroup_Certi_Count = new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // 월 ~ 일 (매일)
                if (checkedId == R.id.guard_request_radio_button_1)
                {
                    GET_SELECT_GUARD_GENDER = "anyone"; // 상관 없어요

                    Log.e(TAG, "onCheckedChanged: 출발 지점: " + GET_CURRENT_CITIZEN_PATH);
                    Log.e(TAG, "onCheckedChanged: 도착 지점: " + GET_SELECT_DESTINATION);

                    Log.e(TAG, "onCheckedChanged: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER);
                }

                // 월 ~ 금 (주 5일)
                else if (checkedId == R.id.guard_request_radio_button_2)
                {
                    GET_SELECT_GUARD_GENDER = "male"; // 남성 도우미 선택

                    Log.e(TAG, "onCheckedChanged: 출발 지점: " + GET_CURRENT_CITIZEN_PATH);
                    Log.e(TAG, "onCheckedChanged: 도착 지점: " + GET_SELECT_DESTINATION);
                    Log.e(TAG, "onCheckedChanged: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER);
                }

                // 토 ~ 일 (주 2일)
                else if (checkedId == R.id.guard_request_radio_button_3)
                {
                    GET_SELECT_GUARD_GENDER = "female"; // 여성 도우미 선택

                    Log.e(TAG, "onCheckedChanged: 출발 지점: " + GET_CURRENT_CITIZEN_PATH);
                    Log.e(TAG, "onCheckedChanged: 도착 지점: " + GET_SELECT_DESTINATION);
                    Log.e(TAG, "onCheckedChanged: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER);
                }
            }
        };

        // 라디오 그룹 실행
        guard_request_radio_group.setOnCheckedChangeListener(RadioGroup_Certi_Count);

        // 끝 _ 성별 선택 (라디오 그룹)

        // todo: 매칭 시작 버튼
        button_request_safe_guard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_safeGuard_Call_Request.this, Activity_Wait_for_request_guard_response.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // todo: 주소 선택 액티비티에서 입력한 시작 지점, 도착 지점 출력하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case 1000:
                    GET_CURRENT_CITIZEN_PATH = data.getStringExtra("address"); // 출발 지점 변수
                    guard_request_address_start.setText(data.getStringExtra("address"));
                    break;

                case 2000:
                    GET_SELECT_DESTINATION = data.getStringExtra("address"); // 도착 지점 변수
                    guard_request_address_end.setText(data.getStringExtra("address"));


                    break;
            }
        }
    }
}
