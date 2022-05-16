package com.nadeem.chatwithpusher

import com.nadeem.chatwithpusher.model.Message
import com.nadeem.chatwithpusher.model.MessageRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatService {
    @POST("chat/message")
    fun postMessage(
        @Header("Authorization") token : String,
        @Body body: MessageRequest
    ): Call<Void>

    companion object {
        private const val BASE_URL = "http://167.172.93.165/api/"

        fun create(): ChatService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(ChatService::class.java)
        }
    }
}