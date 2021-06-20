package com.example.newsBreeze

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://newsapi.org/"
const val API_KEY="e818f3be076a499c9bc762e39b5a51e8"

 interface NewsInterface{
        @GET("v2/top-headlines?apiKey=$API_KEY")
        fun getHeadlines(@Query("country") country:String,@Query("page") page:Int):Call<News>
//     @GET("v2/top-headlines?apiKey=$API_KEY")
//     fun get1(@Query("country") country:String,@Query("page") page:Int,@Query("language") language:String):Call<News>
    }
//https://newsapi.org/v2/top-headlines?apiKey=e818f3be076a499c9bc762e39b5a51e8&country=in&page=1
    object NewsService{
        val newsinstance:NewsInterface
        init {
            val retrofit=Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
             newsinstance=retrofit.create(NewsInterface::class.java)
        }
    }


