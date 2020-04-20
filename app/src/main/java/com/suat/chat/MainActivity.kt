package com.suat.chat

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.CountDownLatch
import android.util.Base64;
import java.io.ByteArrayOutputStream;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        var GetuserInfo: String = ""
        val file = File(this.getFilesDir().getPath().toString() + "/userInfo.txt")
        file.deleteRecursively()

        if (file.exists()) {
            var lines = file.readLines()
            for (line in lines) {
                GetuserInfo += line
            }
            var sp = GetuserInfo.split("|");
            if (sp.count() >= 2) {
                val intent = Intent(this, MessagesActivity::class.java)
                startActivity(intent);
            }
        }
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA), 1)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                    takePicture()

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {
            }
        }
    }

    private fun takePicture() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            var bitmap: Bitmap? = data?.extras?.get("data") as Bitmap
            imgKullanici.setImageBitmap(bitmap)
        }
    }


    fun btnFotografSec(view: View) {
        var izin = checkPersmission()
        if (izin != true) {
            requestPermission();
        } else {
            takePicture()
        }
    }

    fun btnGiris(view: View) {
//        linlaHeaderProgress.setVisibility(View.VISIBLE);

        var _session: Session = Session(
            kID = -9,
            kAdi = edtKullaniciAdi.text.toString(),
            kSifre = edtSifre.text.toString(),
            kFoto = null
        )

        var gson = Gson()
        var jsonString = gson.toJson(_session)
        val mediaType = "application/json; charset=utf-8".toMediaType()


        val okHttpClient = OkHttpClient()


        val requestBody = jsonString.toRequestBody(mediaType)
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://api.puanimcepte.com/api/GirisYap")
            .build()

        var countDownLatch = CountDownLatch(1);
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string();
                    if (body != "-1") {
                        _session = Session(
                            kID = body!!.toInt(),
                            kAdi = edtKullaniciAdi.text.toString(),
                            kSifre = edtSifre.text.toString(),
                            kFoto = null
                        )
                    } else {
                        _session = Session(
                            kID = -1,
                            kAdi = edtKullaniciAdi.text.toString(),
                            kSifre = edtSifre.text.toString(),
                            kFoto = null
                        )
                    }
                    countDownLatch.countDown();
                }
            }
        })

        countDownLatch.await();
        File(this.getFilesDir().getPath().toString() + "/userInfo.txt").printWriter().use { out ->
            out.println(edtKullaniciAdi.text.toString() + "|" + edtSifre.text.toString())
        }

        //val intent = Intent(this, MessagesActivity::class.java)
        //intent.putExtra("session", _session as Serializable)
        //startActivity(intent);

        val intent = Intent(this, activity_ana::class.java)
        startActivity(intent);



//        linlaHeaderProgress.setVisibility(View.INVISIBLE);

    }


    fun btnKayitOl(view: View) {


        val intent = Intent(this, kayit_ol::class.java)
        startActivity(intent);

           val bitmap = (imgKullanici.getDrawable() as BitmapDrawable).getBitmap()
    }
}