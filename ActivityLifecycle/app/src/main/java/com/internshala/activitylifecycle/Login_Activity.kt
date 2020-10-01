package com.internshala.activitylifecycle

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class Login_Activity : AppCompatActivity() , View.OnClickListener {

    lateinit var txtMobNo: EditText
    lateinit var txtPassword: EditText
    lateinit var btnLogIn: Button

    lateinit var txtForgotPassword: TextView
    lateinit var txtRegister: TextView

    val ValidMobileNumber="7903315549"
    val ValidPassword= arrayOf("steve","bruce","tony","thanos")

    lateinit var sharedPreferences: SharedPreferences

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name) , Context.MODE_PRIVATE)

        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn" , false)
        setContentView(R.layout.activity_login)
        if(isLoggedIn)
        {
            val intent=Intent(this@Login_Activity , AvengersActivity::class.java)
            startActivity(intent)
        }

        title="Log in"

        txtMobNo = findViewById(R.id.txtMobNo)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogIn = findViewById(R.id.btnLogIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegister = findViewById(R.id.txtRegister)

        btnLogIn.setOnClickListener{

            val mobilenumber=txtMobNo.text.toString()
            val password=txtPassword.text.toString()
            var nameofAvenger:String

            val intent=Intent(this@Login_Activity , AvengersActivity::class.java)

            if( (mobilenumber==ValidMobileNumber)) {
                when(password) {
                    ValidPassword[0] -> {
                        nameofAvenger = "Captain America"
                        savePreferences(nameofAvenger)
                        startActivity(intent)
                    }
                    ValidPassword[1] -> {
                        nameofAvenger = "Hulk"
                        savePreferences(nameofAvenger)
                        startActivity(intent)
                    }
                    ValidPassword[2] -> {
                        nameofAvenger = "Iron Man"
                        savePreferences(nameofAvenger)
                        startActivity(intent)
                    }
                    ValidPassword[3] -> {
                        nameofAvenger = "The Avengers"
                        savePreferences(nameofAvenger)
                        startActivity(intent)
                    }
                }

            } else {
                Toast.makeText(
                    this@Login_Activity  ,
                    "incorrect credentials" ,
                    Toast.LENGTH_LONG).show()
            }
        }



    }
    override fun onPause() {
        super.onPause()
        finish()
    }

    fun savePreferences(title:String)
    {
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("Title",title).apply()
    }
}