package com.example.readsms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegistRetrofit {
    @POST("POST")
    Call<RegistrSet> PosrData (@Body RegistrSet registrSet);
}
