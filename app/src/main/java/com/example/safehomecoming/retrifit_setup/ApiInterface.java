package com.example.safehomecoming.retrifit_setup;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
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
    Call<Resultm> requestinfo();

    // 안심이가 수락 했을때 update
    @GET("helper_accpet_update.php")
    Call<Resultm> reqeustaccept(@Query("id") int id);
}


