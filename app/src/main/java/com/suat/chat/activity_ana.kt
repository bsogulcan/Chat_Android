package com.suat.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ana.*

class activity_ana : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana)


        navigation_view.setOnNavigationItemSelectedListener {item ->

            var message=""
            when(item.itemId){
                R.id.action_home->message="Home"
                R.id.action_notif->message="Notif"
                R.id.settings->message="Settings"

            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            return@setOnNavigationItemSelectedListener true
        }
    }
}
