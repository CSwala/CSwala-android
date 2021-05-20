package com.cswala.cswala.Adapters;


import com.cswala.cswala.Models.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("codeforces")
    Call<List<Results>> getcodeforces();

    @GET("top_coder")
    Call<List<Results>> gettop_coder();

    @GET("at_coder")
    Call<List<Results>> getat_coder();

    @GET("code_chef")
    Call<List<Results>> getcode_chef();

    @GET("hacker_rank")
    Call<List<Results>> gethacker_rank();

    @GET("hacker_earth")
    Call<List<Results>> gethacker_earth();

    @GET("kick_start")
    Call<List<Results>> getkick_start();

    @GET("leet_code")
    Call<List<Results>> getleet_code();
}
