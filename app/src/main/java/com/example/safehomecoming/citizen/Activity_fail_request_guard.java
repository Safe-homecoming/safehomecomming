package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.safehomecoming.Emergency_Mode;
import com.example.safehomecoming.R;

import static com.example.safehomecoming.citizen.Activity_Wait_for_request_guard_response.GET_FAIL_MESSAGE;

public class Activity_fail_request_guard extends AppCompatActivity
{

    private TextView
            fail_message        // 실패사유 메시지
            ,fail_cancel        // 액티비티 닫기
            , button_protected  //
            , button_rematch    //
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_request_guard);

        fail_cancel = findViewById(R.id.fail_cancel);
        fail_message = findViewById(R.id.fail_message);
        button_protected = findViewById(R.id.button_protected);
        button_rematch = findViewById(R.id.button_rematch);

        fail_message.setText(GET_FAIL_MESSAGE);

        // 액티비티 닫기
        fail_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        // todo: 매칭 다시 하기 위해 화면 닫기
        button_rematch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_fail_request_guard.this, Activity_Wait_for_request_guard_response.class);
                startActivity(intent);
                finish();
            }
        });

        // 경계모드로 넘어가기
        button_protected.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Activity_fail_request_guard.this, Emergency_Mode.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
