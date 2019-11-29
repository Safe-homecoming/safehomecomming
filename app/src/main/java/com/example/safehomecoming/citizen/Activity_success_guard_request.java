package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.safehomecoming.R;

import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_CITIZEN_PATH;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_DESTINATION;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_GUARD_GENDER;

public class Activity_success_guard_request extends AppCompatActivity
{
    private String TAG = "Activity_success_guard_request";

    private TextView button_cancel_success_guard_request;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_guard_request);

        button_cancel_success_guard_request = findViewById(R.id.button_cancel_success_guard_request);

        Log.e(TAG, "onCreate: 출발 지점: " + GET_CURRENT_CITIZEN_PATH );
        Log.e(TAG, "onCreate: 도착 지점: " + GET_SELECT_DESTINATION );
        Log.e(TAG, "onCreate: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER );

        // 매칭 화면 닫기
        button_cancel_success_guard_request.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 액티비티 종료하기
                Activity_Main_Citizen activity_main_citizen = (Activity_Main_Citizen) Activity_Main_Citizen.Activity_Main_Citizen;
                activity_main_citizen.finish();

                Intent intent = new Intent(Activity_success_guard_request.this, Activity_Main_Citizen.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
