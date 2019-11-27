package com.example.safehomecoming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.safehomecoming.citizen.Activity_Main_Citizen;
import com.example.safehomecoming.safeGurad.Activity_Main_Guard;

public class Activity_Login extends AppCompatActivity
{

    private TextView
            login_button // 실제 로그인 버튼
            , citizon    // 임시 로그인 버튼 _ 시민
            , guard      // 임시 로그인 버튼 _ 도우미
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.login_button);
        citizon = findViewById(R.id.citizon);
        guard = findViewById(R.id.guard);

        // 로그인 버튼
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // 로그인 기능 구현해야됨
            }
        });

        // 버튼 _ 시민 화면 바로가기 (임시 버튼)
        citizon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Activity_Login.this, Activity_Main_Citizen.class);
                startActivity(intent);
                finish();
            }
        });

        // 버튼 _ 도우미 화면 바로가기 (임시 버튼)
        guard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Activity_Login.this, Activity_Main_Guard.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
