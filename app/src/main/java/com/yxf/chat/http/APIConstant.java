package com.yxf.chat.http;

import com.yxf.chat.appBase.ChatConfig;
import com.yxf.chat.home.bean.TokenBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 各种借口请求方法
 *
 * @author xiong
 * @name APIConstant
 * @date 2017/9/11
 */
public interface APIConstant {
    @POST(ChatConfig.GET_TOKEN)
    Call<TokenBean> getToken(
            @Query("userId") String userId,
            @Query("name") String name,
            @Query("portraitUri") String userIcon
    );

//    @POST(ChatConfig.GET_TOKEN)
//    Call<TokenBean> getToken(
//            @Body TokenBody body
//            );
}
