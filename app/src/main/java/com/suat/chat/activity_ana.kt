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
                R.id.action_kanal->message="Home"
                R.id.action_ara->message="Notif"
                R.id.action_profil->message="Settings"

            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction().replace(R.id.frame,AnaFragment.newInstance("asdas","asdasdf")).commit()
            return@setOnNavigationItemSelectedListener true
        }
    }
}
