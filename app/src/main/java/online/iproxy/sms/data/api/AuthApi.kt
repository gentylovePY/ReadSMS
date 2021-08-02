package online.iproxy.sms.data.api

import online.iproxy.sms.data.api.model.LoginRequest
import online.iproxy.sms.data.api.model.LoginResponse
import retrofit2.http.POST

interface AuthApi {

    @POST("1/login")
    suspend fun login(request: LoginRequest.Password): LoginResponse

    @POST("1/login")
    suspend fun login(request: LoginRequest.Pin): LoginResponse

}