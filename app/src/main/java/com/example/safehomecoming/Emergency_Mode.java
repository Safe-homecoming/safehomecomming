package com.example.safehomecoming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.L;

public class Emergency_Mode extends AppCompatActivity {

    int cnt =0; // 볼륨 버튼 클릭 횟수 확인
    int recivecnt;
    int check  =0 ;// 버튼 체크
    private LinearLayout all_Layouts, mode_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_mode);

        all_Layouts = (LinearLayout)findViewById(R.id.L_layout);
        mode_lay = (LinearLayout)findViewById(R.id.mode_onoff);

        // 잠금 화면에 올릴때 필요함
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        all_Layouts.setBackgroundColor(Color.WHITE);

        // getIntent
        Intent  intent = getIntent(); // ScreenReceiver.class
        //  잠금화면에 올려질때  현재 액티비티가  새로 올려지기 때문에
        // receiver에서 putextra 로 cnt값을 보내줌  받은 값으로 경계모드 활성으로 보여짐
        recivecnt  = intent.getIntExtra("cnt",0);
        if( recivecnt != 0) { // 매칭 대기하기 화면
            all_Layouts.setBackgroundColor(Color.RED);
            check =1;
        }




        // 경계모드 활성화 시키기
        mode_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(check  == 0) {
                   Toast.makeText(Emergency_Mode.this, " 경계모드가 활성화 되었습니다. ", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                   startService(intent);
                   all_Layouts.setBackgroundColor(Color.RED);
                   check = 1;
               }else if(check == 1){
                   Toast.makeText(Emergency_Mode.this, " 경계모드가 비활성화 되었습니다. ", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                   stopService(intent);
                   all_Layouts.setBackgroundColor(Color.WHITE);
                   check = 0;
               }


            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(check == 1) {  // 활성모드로 되었을떄 긴급이 진행됨
                    // Toast.makeText(this, "KeyDown", Toast.LENGTH_SHORT).show();
                    cnt++;
                    Log.i("MODE CHEC", "check check Down KeyDown Key" + cnt);
                    if (cnt == 3) {
                        EmEmode();
                    }
                }

                break;

            case KeyEvent.KEYCODE_VOLUME_UP:
                if(check == 1) {  // 활성모드로 되었을떄 긴급이 진행됨
                    //Toast.makeText(this, "KeyUp", Toast.LENGTH_SHORT).show();
                    cnt++;
                    Log.i("MODE CHEC", "check check Up KeyUp KeyUp KeyUp Key" + cnt);
                    if (cnt == 3) {
                        EmEmode();
                    }
                }
                break;
        }
        return true;

      //  return super.onKeyDown(keyCode, event);
    }

    // 긴급모드 메소드
    private void EmEmode(){
        all_Layouts.setBackgroundColor(Color.RED);
        // 기존 잠금화면 위로 경계모드 올려주기

        Toast.makeText(this, " 긴급모드가 활성화 되었습니다. ", Toast.LENGTH_SHORT).show();

    }
}
