package api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val BASE_URL = "https://stg-b2b.api.sharetrip.net/"
    private val logger: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val okClient = OkHttpClient.Builder().addInterceptor(logger).build()

    private val retrofit = Retrofit.Builder()
        .client(okClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> callService(service: Class<T>): T {
        return retrofit.create(service)
    }
}