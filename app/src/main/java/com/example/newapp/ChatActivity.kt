package com.example.newapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.viewModels


//data class ChatMessage(
//    val text: String,
//    val isMine: Boolean
//)

object ChatViewModelHolder {
    lateinit var viewModel: ChatViewModel
}

class ChatActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()


    companion object {
//        val messages = mutableStateListOf<ChatMessage>()

        var sendCallback: ((String) -> Unit)? = null
        var closeConnection: (() -> Unit)? = null

        var onConnectionLost: (() -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onConnectionLost = {
            finish()
        }

        ChatViewModelHolder.viewModel = viewModel

        setContent {
            MaterialTheme {

//                val viewModel: ChatViewModel = viewModel()

                ChatScreen(
                    viewModel = viewModel,
                    onBackClick = {
                        closeConnection?.invoke()
                        finish()
                    }
                )
            }
        }
    }

    override fun onBackPressed() {
        closeConnection?.invoke()

        super.onBackPressed()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit
) {

    val messages = viewModel.messages
    var inputText by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messages) { message ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        contentAlignment =
                            if (message.isMine)
                                Alignment.CenterEnd
                            else
                                Alignment.CenterStart
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color =
                                if (message.isMine)
                                    Color(0xFFDCF8C6) // green (me)
                                else
                                    Color(0xFFFFFFFF) // white (other)
                        ) {
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(10.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {

                            viewModel.sendMessage(inputText)

                            ChatActivity.sendCallback?.invoke(inputText)

                            inputText = ""
                        }
                    }
                ) {
                    Text("Send")
                }
            }
        }
    }
}
