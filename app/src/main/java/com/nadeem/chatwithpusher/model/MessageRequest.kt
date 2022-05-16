package com.nadeem.chatwithpusher.model

data class MessageRequest(
    var conversationId: String,
    var receiverId: String,
    var message: String
)