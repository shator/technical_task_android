package com.example.task.data.api

import com.example.task.BuildConfig
import com.example.task.data.models.NetworkResult
import com.example.task.data.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface UsersService {

    @GET("/public/v2/users")
    suspend fun getUsers(
        @Query("page") page: Int
    ): Response<List<User>>

    @POST("/public/v2/users")
    suspend fun addUser(
        @Body user: User
    ): Response<User>

    @DELETE("/public/v2/users/{id}")
    suspend fun deleteUser(
        @Path("id") userId: Int
    ): Response<List<User>>


    companion object {
        fun create(): UsersService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}").build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UsersService::class.java)
        }

        suspend fun <T : Any> handleApi(
            execute: suspend () -> Response<T>
        ): NetworkResult<T> {
            return try {
                val response = execute()
                val body = response.body()
                if (response.isSuccessful) {
                    NetworkResult.ApiSuccess(body, response.code())
                } else {
                    NetworkResult.ApiError(response.code(), response.message())
                }
            } catch (e: HttpException) {
                NetworkResult.ApiError(e.code(), e.message())
            } catch (e: Throwable) {
                NetworkResult.ApiException(e)
            }
        }
    }
}