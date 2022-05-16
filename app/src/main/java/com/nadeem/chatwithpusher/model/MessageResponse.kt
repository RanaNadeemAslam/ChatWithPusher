package com.nadeem.chatwithpusher.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("status_code" ) var statusCode : Int?     = null,
    @SerializedName("message"     ) var message    : Message? = Message()
)
