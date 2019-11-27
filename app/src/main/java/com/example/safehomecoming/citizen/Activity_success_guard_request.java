package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.safehomecoming.R;

import static com.example.safehomecoming.citizen.Activity_Main_Citizen.GET_CURRENT_CITIZEN_PATH;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_DESTINATION;
import static com.example.safehomecoming.citizen.Activity_safeGuard_Call_Request.GET_SELECT_GUARD_GENDER;

public class Activity_success_guard_request extends AppCompatActivity
{
    private String TAG = "Activity_success_guard_request";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_guard_request);

        Log.e(TAG, "onCreate: 출발 지점: " + GET_CURRENT_CITIZEN_PATH );
        Log.e(TAG, "onCreate: 도착 지점: " + GET_SELECT_DESTINATION );
        Log.e(TAG, "onCreate: 요청할 도우미 성별: " + GET_SELECT_GUARD_GENDER );
    }
}
