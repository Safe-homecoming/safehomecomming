package com.example.safehomecoming;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safehomecoming.retrifit_setup.ApiClient;
import com.example.safehomecoming.retrifit_setup.ApiInterface;
import com.example.safehomecoming.retrifit_setup.Resultm;
import com.example.safehomecoming.safeGurad.Accept_Data;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.Arrays;
import java.util.List;

public class WorkCheck extends AppCompatActivity {


    String wstatus ;
    String results; //쉐어드 프리퍼런스
    String results_wstatus; //쉐어드 프리퍼런스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_check);


        // 스위치 버튼 상태에 따라 문자열을 출력할 텍스트뷰입니다.
        final TextView optionState = (TextView)findViewById(R.id.textView);

        // SharedPreference 를 선언한다.
        // 저장했을때와 같은 key로 xml에 접근한다.
                SharedPreferences pref = getSharedPreferences("meminfo", MODE_PRIVATE);

        // key에 해당한 value를 불러온다.
        // 두번째 매개변수는 , key에 해당하는 value값이 없을 때에는 이 값으로 대체한다.
                results = pref.getString("memId", "");
                results_wstatus = pref.getString("wstatus", "");


        // 스위치 버튼입니다.
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.sb_use_listener);

        switchButton.setChecked(Boolean.parseBoolean(results_wstatus));
        // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
        if (results_wstatus.equals("true")){

            optionState.setText("안심이 활동 중입니다");
        }else{
            optionState.setText("안심이 활동을 쉬고 있습니다.");

        }

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getSharedPreferences("meminfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){

                    optionState.setText("안심이 활동 중입니다");

                    wstatus = "true";
                    //쉐어드 값 변경
                    editor.putString("wstatus", "true");

                    // 메모리에 있는 데이터를 저장장치에 저장한다.
                    editor.commit();
                }else{

                    optionState.setText("안심이 활동을 쉬고 있습니다.");
                    wstatus = "false";
                    //쉐어드 값 변경
                    editor.putString("wstatus", "false");

                    // 메모리에 있는 데이터를 저장장치에 저장한다.
                    editor.commit();
                }

                // 일상태 없데이트
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Resultm> call = apiInterface.wupdate(results,wstatus);

                call.enqueue(new Callback<Resultm>() {
                    @Override
                    public void onResponse(Call<Resultm> call, Response<Resultm> response) {

                        //정상 결과
                        Resultm result = response.body();
                        String result_code = response.body().getResult_code();

                        if (response.body() != null) {
                            // if (result.getResult().equals("ok")) {
                            if (result_code.equals("100")) {

                                    Log.i("workcheck","일 상태 업데이트 완료");


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
                        Log.e(" 에러 발생 Log ", t.getMessage());
                    }
                });



            }
        });

    }
}
