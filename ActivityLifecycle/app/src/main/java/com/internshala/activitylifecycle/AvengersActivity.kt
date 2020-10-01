package com.internshala.activitylifecycle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AvengersActivity : AppCompatActivity() {
    var titlename:String?="Avengers"
    lateinit var sharedPreferences:SharedPreferences
    lateinit var btnLogOut:Button

    lateinit var btnSend:Button
    lateinit var txtMsg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scroll_view_example)

      sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name) , Context.MODE_PRIVATE)

        titlename = sharedPreferences.getString("Title","The Avengers")

        title=titlename
        
        btnLogOut=findViewById(R.id.btnLogOut)
        btnSend = findViewById(R.id.btnSend)
        txtMsg=findViewById(R.id.txtMsg)

        btnLogOut.setOnClickListener()
        {
            sharedPreferences.edit().clear().apply()
            Toast.makeText(this@AvengersActivity,"Logged out" , Toast.LENGTH_LONG).show()
            finish()
        }

        btnSend.setOnClickListener()
        {
            var message=txtMsg.text.toString()
            val intent = Intent(this@AvengersActivity , MessageActivity::class.java)
            intent.putExtra("Message" , message)

            startActivity(intent)
        }
    }

}
/*if(intent!=null)
        {
        titlename=intent.getStringExtra("Name")
        }
        title=titlename
        this code was previously used to give the title
        */