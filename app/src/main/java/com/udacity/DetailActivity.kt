package com.udacity

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val fileText = findViewById<TextView>(R.id.filename)
        val statusText = findViewById<TextView>(R.id.status)
        val button = findViewById<Button>(R.id.button)
        val bundle: Bundle? = intent.extras
        bundle?.let {

            bundle.apply {
                //Intent with data
//                val string: String? = getString("keyString")
                val status:Int? = getInt("status")

                val filename:String? = getString("file")!!
                fileText.text = filename
                statusText.text = when(status){
                    8 -> "Success"
                    else -> "Fail"
                }
                    Log.i("extras", status.toString())
                Log.i("extras", filename.toString())

            }}
        val mainIntent = Intent(this, MainActivity::class.java)


        button.setOnClickListener {
            startActivity(mainIntent)
        }


    }

}
