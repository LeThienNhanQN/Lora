package com.ldnhat.loraapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ldnhat.loraapp.common.model.ApiResponse
import com.ldnhat.loraapp.common.model.Data
import com.ldnhat.loraapp.common.model.Node
import com.ldnhat.loraapp.common.model.SignIn
import com.ldnhat.loraapp.dto.sign_in.SignInRequestDTO
import com.ldnhat.loraapp.utils.constants.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

interface LoginApiService {
    @POST("user/authentication/sign-in")
    fun userSignInAsync(@Body signInRequestDTO: SignInRequestDTO): Deferred<ApiResponse<SignIn>>
}

interface DataApiService {
    @GET("user/data")
    fun userGetAllDataAsync(
        @Header("Authorization") authHeader: String,
        @Query("nodeId") nodeId: String?
    ): Deferred<List<Data>>
}

interface NodeApiService {
    @GET("user/node")
    fun userGetAllNodeAsync(@Header("Authorization") authHeader: String): Deferred<List<Node>>
}

interface LedApiService {
    @PUT("user/led/token-key/{id}")
    fun userChangeLedStateAsync(
        @Path("id") deviceId: String,
        @Header("Authorization") authHeader: String
    ): Deferred<ApiResponse<String>>
}

object LoginApi {
    val loginApiService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}

object DataApi {
    val dataApiService: DataApiService by lazy {
        retrofit.create(DataApiService::class.java)
    }
}

object NodeApi {
    val nodeApiService: NodeApiService by lazy {
        retrofit.create(NodeApiService::class.java)
    }
}

object LedApi {
    val ledApiService: LedApiService by lazy {
        retrofit.create(LedApiService::class.java)
    }
}
