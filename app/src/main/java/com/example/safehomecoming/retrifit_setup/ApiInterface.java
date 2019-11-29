package com.example.safehomecoming.retrifit_setup;



import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface
{
    // ex) 사진 목록 불러오기
//    @FormUrlEncoded
//    @POST("getImage.php")
//    Call<responseHelperInfo> getHelperInfo(
//            @Field("userId") String userId
//            , @Field("userAddress") String userAddress
//            , @Field("userLatitude") String userLatitude
//            , @Field("userLongitude") String userLongitude
//            , @Field("reqGender") String reqGender
//    );

    // 안심이 신청 현황 내용
    @GET("requstcitizen.php")
    Call<Resultm> requestinfo(@Query("gender") String  gender);

    // 안심이가 수락 했을때 update
    @GET("helper_accpet_update.php")
    Call<Resultm> reqeustaccept(@Query("id") int id);

    //회원 로그인
    @FormUrlEncoded
    @POST("memberchek.php")
    Call<Resultm> logincheck(@FieldMap HashMap<String, Object> param);

    // 안심이 출퇴근 체크 update
    @GET("wstatus_update.php")
    Call<Resultm> wupdate(@Query("memid") String memid , @Query("wstatus") String  wstatus);

    // 안심이 위치  update
    @GET("helper_location_update.php")
    Call<Resultm> hlocationupdate(@Query("memid") String memid , @Query("latitude") String  latitude, @Query("longitude") String  longitude);

    //안심이 귀가 완료 버튼
    @GET("helperfinish.php")
    Call<Resultm> finishsuccess(@Query("idx") int idx);
}


