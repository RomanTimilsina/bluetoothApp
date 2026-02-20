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

data class ChatMessage(
    val text: String,
    val isMine: Boolean
)

class ChatActivity : ComponentActivity() {



    companion object {
        val messages = mutableStateListOf<ChatMessage>()

        var sendCallback: ((String) -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ChatScreen(
                    onBackClick = {
                        BluetoothConnectionManager.closeConnection()
                        finish()
                    }
                )
            }
        }
    }

    override fun onBackPressed() {
        BluetoothConnectionManager.closeConnection()
        super.onBackPressed()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onBackClick: () -> Unit) {

    val messages = ChatActivity.messages
    var inputText by remember { mutableStateOf("") }

    val context = LocalContext.current

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

                            ChatActivity.messages.add(
                                ChatMessage(
                                    text = inputText,
                                    isMine = true
                                )
                            )

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
