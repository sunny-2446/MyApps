package com.internshala.activitylifecycle

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class MessageActivity : AppCompatActivity() {

    var message:String?=null
    lateinit var UserMessage:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        UserMessage=findViewById(R.id.UserMessage)
        if(intent!=null) {
           message = intent.getStringExtra("Message")

            UserMessage.text = message
        }
        title="Chats"
    }
}