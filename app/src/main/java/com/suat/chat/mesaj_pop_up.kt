package com.suat.chat

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_mesaj_pop_up.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.util.concurrent.CountDownLatch
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import java.io.IOException
import android.util.Base64;

class mesaj_pop_up : Activity() {
    var _session:Session?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mesaj_pop_up)

        var intent = getIntent();
        val bundle: Bundle? = intent.extras
        var kAdi = intent.getStringExtra("kullaniciAdi")

        _session= Session(kID = null,kSifre = null,kFoto = null,kAdi = kAdi)

        lblKullaniciAdi.setText(kAdi)
        var dm=DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        var width=(dm.widthPixels*.8).toInt();
        var height =(dm.heightPixels*.7).toInt();

        window.setLayout(width,height)

        var params=window.attributes
        params.gravity=Gravity.CENTER
        params.x=0
        params.y=-20

        window.attributes=params



        var gson = Gson()
        var jsonString = gson.toJson(_session)
        val mediaType = "application/json; charset=utf-8".toMediaType()


        val okHttpClient = OkHttpClient()


        val requestBody = jsonString.toRequestBody(mediaType)
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://api.puanimcepte.com/api/KullaniciDetay")
            .build()

        var countDownLatch = CountDownLatch(1);
        var userInfos:Session?=null
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if(response.isSuccessful){
                    val body = response.body?.string();
                    userInfos = gson.fromJson(body, Session::class.java)
                    countDownLatch.countDown();
                }
            }})
        countDownLatch.await()

        var decodedString = Base64.decode(userInfos!!.kFoto, Base64.DEFAULT)
        var bmp=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        imageKullanici.setImageBitmap(bmp!!)




    }
}
