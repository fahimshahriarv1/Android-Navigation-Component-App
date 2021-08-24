package interfaces

import data.ResponseB2b
import data.ResponseClient
import retrofit2.http.*

interface ApiInterface {
     @FormUrlEncoded
     @POST("api/v1/auth/login/")
     suspend fun getDataPost(@Field ("password") password:String, @Field("username") username: String):ResponseB2b

     @GET("api/v1/payment/history?offset=0&limit=10")
     suspend fun getDataGet(@Header("userToken") userToken:String):ResponseClient
}