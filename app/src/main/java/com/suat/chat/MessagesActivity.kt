package com.suat.chat

import android.content.Context
import android.content.Intent
import android.content.QuickViewConstants
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.message_list.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.LocalDateTime
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.io.Serializable
import java.util.concurrent.CountDownLatch

class MessagesActivity : AppCompatActivity()  {

    var _session:Session?=null

    var msjlr:ArrayList<Mesajlar> = ArrayList()
    var cnt:Context? = null
    var listeYenile:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)


        cnt=this

        var intent = getIntent();
        val bundle: Bundle? = intent.extras
        _session = bundle?.getSerializable("session") as Session


        this@MessagesActivity.runOnUiThread(java.lang.Runnable {
            run("http://api.puanimcepte.com/api/Mesajlar")
            listMesajlar.adapter  = MyListAdapter(cnt!!,msjlr,_session!!)
        })


        timer = object : CountDownTimer(secondsRemaining * 100000, 500) {
            override fun onFinish(){}
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = 1000
                run("http://api.puanimcepte.com/api/Mesajlar")
            }
        }.start()


        listMesajlar.setOnItemClickListener { adapterView, view, position: Int, id: Long ->

            var seciliMesaj = adapterView.getItemAtPosition(position) as Mesajlar
            val intent = Intent(this, mesaj_pop_up::class.java)
            intent.putExtra("kullaniciAdi",seciliMesaj.kAdi as String)
            startActivity(intent)

        }

        
        edtMesaj.requestFocus()
    }
    private lateinit var timer: CountDownTimer
    private var secondsRemaining: Long = 10

    fun MesajGetir(_mesajlar:MutableList<Mesajlar>) {
        var mesaySayisi = _mesajlar.count() - 1
        if  (msjlr.count()>0) {
            var listedekiSonMesaj = msjlr[msjlr.lastIndex].Icerik.toString()
            var serverdakiSonMesaj = _mesajlar[mesaySayisi].Icerik.toString()

            if (listedekiSonMesaj!=serverdakiSonMesaj) {
                msjlr.clear()
                for (k in 0..mesaySayisi) {
                    msjlr.add(
                        Mesajlar(
                            _mesajlar[k].kID?.toInt(),
                            _mesajlar[k].kAdi.toString(),
                            _mesajlar[k].Icerik.toString(),
                            _mesajlar[k].Tarih.toString()
                        )
                    )
                }
                this@MessagesActivity.runOnUiThread(java.lang.Runnable {
                    listMesajlar.adapter = MyListAdapter(cnt!!, msjlr,_session!!)
                })
            }
        } else{
            for (k in 0..mesaySayisi) {
                msjlr.add(
                    Mesajlar(
                        _mesajlar[k].kID?.toInt(),
                        _mesajlar[k].kAdi.toString(),
                        _mesajlar[k].Icerik.toString(),
                        _mesajlar[k].Tarih.toString()
                    )
                )
            }

        }
    }

    fun run(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body?.string();
                val gson= GsonBuilder().create()
                var _mesajlar = gson.fromJson(body, Array<Mesajlar>::class.java).toMutableList()
                MesajGetir(_mesajlar)
            }})
    }


    fun btnMesajGonder(view: View){
        val dtFormat = SimpleDateFormat("M.dd.yyyy hh:mm:ss")
        val currentDate = dtFormat.format(Date())
        SendMessage(1,edtMesaj.text.toString(),currentDate,view)
    }


    fun SendMessage(kID:Int,Mesaj:String,Tarih:String,view: View) {

        var gson = Gson()
        var jsonString = gson.toJson(Mesajlar(kID=_session!!.kID,kAdi = _session!!.kAdi,Icerik = Mesaj,Tarih = Tarih))
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val okHttpClient = OkHttpClient()
        val requestBody = jsonString.toRequestBody(mediaType)
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://api.puanimcepte.com/api/Mesajlar")
            .build()

        var countDownLatch = CountDownLatch(1);
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }
            override fun onResponse(call: Call, response: okhttp3.Response) {
                    val body = response.body?.string();
                    this@MessagesActivity.runOnUiThread(java.lang.Runnable {
                        edtMesaj.text.clear()
                    })

                countDownLatch.countDown();
            }
        })
        countDownLatch.await()
    }

    fun listMessajlarOnClick(view: View){
        //edtMesaj.clearFocus()
    }
}