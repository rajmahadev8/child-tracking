package com.infinity.childtracking.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static String url = "http://157.245.106.192/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
