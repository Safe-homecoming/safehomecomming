package com.example.safehomecoming.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.safehomecoming.R;

import static com.example.safehomecoming.citizen.Activity_Wait_for_request_guard_response.GET_FAIL_MESSAGE;

public class Activity_fail_request_guard extends AppCompatActivity
{

    private TextView
            fail_message    // 실패사유 메시지
            ,fail_cancel    // 액티비티 닫기
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_request_guard);

        fail_cancel = findViewById(R.id.fail_cancel);
        fail_message = findViewById(R.id.fail_message);

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
    }
}
