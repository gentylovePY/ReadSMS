package com.example.readsms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
@POST("login")
    Call<UserResponce> saveuser (@Body UserRequest userRequest);
}
