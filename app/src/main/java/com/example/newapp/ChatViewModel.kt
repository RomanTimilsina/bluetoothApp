package com.example.newapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class ChatMessage(
    val text: String,
    val isMine: Boolean
)



class ChatViewModel: ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()

    val messages: List<ChatMessage> = _messages

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        _messages.add(
            ChatMessage(
                text = text,
                isMine = true
            )
        )
    }

    fun receiveMessage(text: String) {
        _messages.add(
            ChatMessage(
                text = text,
                isMine = false
            )
        )
    }
}