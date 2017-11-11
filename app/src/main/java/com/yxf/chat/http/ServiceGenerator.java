package com.yxf.chat.http;

import com.yxf.chat.appBase.ChatConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络访问生成库
 *
 * @author xiong
 * @name ServiceGenerator
 * @date 2017/9/9
 */
public class ServiceGenerator {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(ChatConfig.HOST)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
