package com.nadeem.chatwithpusher.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("conversationId" ) var conversationId : Int? = null,
    @SerializedName("message"        ) var message        : String? = null,
    @SerializedName("senderId"       ) var senderId       : Int?    = null,
    @SerializedName("receiverId"     ) var receiverId     : String? = null,
    @SerializedName("updated_at"     ) var updatedAt      : String? = null,
    @SerializedName("created_at"     ) var createdAt      : String? = null,
    @SerializedName("id"             ) var id             : Int?    = null,
    @SerializedName("order_id"       ) var orderId        : Int?    = null
)