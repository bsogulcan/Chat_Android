package com.suat.chat

import kotlinx.android.synthetic.main.activity_kayit_ol.*

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.CountDownLatch
import android.util.Base64;
import kotlinx.android.synthetic.main.activity_kayit_ol.edtKullaniciAdi
import kotlinx.android.synthetic.main.activity_kayit_ol.edtSifre
import kotlinx.android.synthetic.main.activity_kayit_ol.imgKullanici
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream;

class kayit_ol : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol)
    }

    fun btnFotografSec(view: View){
        //pickImageFromGallery()
        var izin = checkPersmission()
        if (izin != true) {
            requestPermission();
        } else {
            takePicture()
        }
    }

    private fun pickImageFromGallery() {





        /*val intent = Intent(Intent.ACTION_PICK)
        intent.type = "download/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)*/
         */

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

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA), 1)
    }

    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }



    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){

            imgKullanici.setImageURI(data?.data)
        }
    } */

    fun GirisYap(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    fun KayitOl(view: View){
        val bitmap = (imgKullanici.getDrawable() as BitmapDrawable).getBitmap()

        var byteArrayOutputStream = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 10, byteArrayOutputStream);
        var byteArray = byteArrayOutputStream.toByteArray();
        var encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


        var _session:Session=Session(kID=-9,kAdi=edtKullaniciAdi.text.toString(),kSifre = edtSifre.text.toString(),kFoto = encoded)

        var gson = Gson()
        var jsonString = gson.toJson(_session)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val okHttpClient = OkHttpClient()
        val requestBody = jsonString.toRequestBody(mediaType)
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://api.puanimcepte.com/api/KayitOl")
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if(response.isSuccessful){
                    val body = response.body?.string();
                    if(body!="-1")
                    {
                        //_session=Session(kID=body!!.toInt(),kAdi=edtKullaniciAdi.text.toString(),kSifre = edtSifre.text.toString(),kFoto =  null)
                    }else {
                        //_session=Session(kID=-1,kAdi=edtKullaniciAdi.text.toString(),kSifre = edtSifre.text.toString(),kFoto =  null)
                    }
                }
            }})
    }

}

