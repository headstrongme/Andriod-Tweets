package com.example.andriodtweets

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : AppCompatActivity()  {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var progressbar: ProgressBar

    // it enables the login button using textWatcher when condition is fulfilled
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            val inputtedusername: String = username.getText().toString().trim()
            val inputedpassword: String = password.text.toString().trim()
            val enablebutton: Boolean = inputtedusername.isNotEmpty()&& inputedpassword.isNotEmpty()

            login.isEnabled=enablebutton
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity" , "on Create called" )

        username =findViewById(R.id.username)
        password=findViewById(R.id.password)
        login=findViewById(R.id.login)
       progressbar=findViewById(R.id.progressBar)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

        login.setOnClickListener{
            Log.d("MainActivity" , "on click called" )
            progressbar.visibility = View.VISIBLE

            val intent: Intent = Intent(this, TweetActivity::class.java)
            intent.putExtra("loaction", "Washington D.C.")
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

    }
}
