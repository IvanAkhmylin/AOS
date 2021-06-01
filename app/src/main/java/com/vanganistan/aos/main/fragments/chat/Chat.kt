package com.vanganistan.aos.main.fragments.chat

data class Chat(var senderId: String? = "",
                val senderName: String? = "",
                val senderImage: String? = "",
                var message: String? = "",
                var isseen: Boolean? = false,
                var date: Long? = 0L
                )