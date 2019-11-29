package com.example.safehomecoming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safehomecoming.citizen.Activity_Main_Citizen;
import com.example.safehomecoming.retrifit_setup.ApiClient;
import com.example.safehomecoming.retrifit_setup.ApiInterface;
import com.example.safehomecoming.retrifit_setup.Resultm;
import com.example.safehomecoming.safeGurad.Activity_Main_Guard;

import java.util.HashMap;

public class Activity_Login extends AppCompatActivity
{

    private Button loginbtn, singupbtn;
    private EditText textId, textPw;
    private String TAG = "Activity_Login";

    public static String GET_USER_ID // id
            ,           GET_TOKEN
            ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.login_button);  //로그인 버튼
        //singupbtn = findViewById(R.id.go_reg); // 가입버튼
        textId = findViewById(R.id.login_id);// id 입력
        textPw = findViewById(R.id.login_password);// pw 입력

        // 로그인 버튼  안심인지 시민인지 자동으로 여기서 구분
        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap<String, Object> input = new HashMap<>();
                input.put("id", textId.getText().toString());
                input.put("password", textPw.getText().toString());
                input.put("title", "this is title");
                input.put("body", "this is body");

                GET_USER_ID = (String) input.get("id");

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Resultm> call = apiInterface.logincheck(input);

                call.enqueue(new Callback<Resultm>()
                {
                    @Override
                    public void onResponse(@NonNull Call<Resultm> call, Response<Resultm> response)
                    {
                        //정상 결과
                        Resultm result = response.body();
                        String memtype = response.body().getMemtype();
                        GET_TOKEN = response.body().getToken();

                        Log.e(TAG, "onResponse: token: " + GET_TOKEN);

                        if (response.body() != null)
                        {
                            if (result.getResult().equals("ok"))
                            {
                                /// 멤버 타입으로 시민인지 안심인지 구분하기
                                if (memtype.equals("helper"))
                                {  //안심이
                                    Intent intent = new Intent(Activity_Login.this, Activity_Main_Guard.class);
                                    startActivity(intent);
                                    finish();
                                } else if (memtype.equals("citizen"))
                                {//시민일때
                                    Intent intent = new Intent(Activity_Login.this, Activity_Main_Citizen.class);
                                    //intent.putExtra("imageUri", uri);
                                    startActivity(intent);
                                    finish();
                                }

                                //회원 기본정보 쉐어드에 저장하기
                                SharedPreferences pref = getSharedPreferences("meminfo", MODE_PRIVATE);

                                // SharedPreferences 의 데이터를 저장/편집 하기위해 Editor 변수를 선언한다.
                                SharedPreferences.Editor editor = pref.edit();

                                // key값에 value값을 저장한다.
                                // String, boolean, int, float, long 값 모두 저장가능하다.
                                editor.putString("memId", response.body().getMemId());
                                editor.putString("phone", response.body().getPhone());
                                editor.putString("gender", response.body().getGender());
                                editor.putString("age", response.body().getAge());
                                editor.putString("name", response.body().getName());
                                editor.putString("memtype", response.body().getMemtype());
                                editor.putString("wstatus", response.body().getWstatus());
                                editor.putString("token", response.body().getToken());
                                // 메모리에 있는 데이터를 저장장치에 저장한다.
                                editor.commit();

                            }
                            else
                            {
                                Toast.makeText(Activity_Login.this, "로그인이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Resultm> call, Throwable t)
                    {
                        //네트워크 문제
                        Toast.makeText(Activity_Login.this, "네트워크 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
