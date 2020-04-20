package com.suat.chat

import android.content.Context
import android.content.Intent
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_messages.*


class MyListAdapter(var context:Context,var mesajlar: ArrayList<Mesajlar>,var _session:Session):BaseAdapter(){
    private class  ViewHolder(row:View?){
    var txtKAdi:TextView
    var txtIcerik:TextView
    //var txtTime:TextView
    var metxtKAdi:TextView
    var metxtIcerik:TextView
    //var metxtTime:TextView
    var meText:LinearLayout
    var otherText:LinearLayout

        init {
            this.txtKAdi=row?.findViewById(R.id.kAdi) as TextView
            this.txtIcerik=row?.findViewById(R.id.icerik) as TextView
            //this.txtTime=row?.findViewById(R.id.time) as TextView
            this.metxtKAdi=row?.findViewById(R.id.mekAdi) as TextView
            this.metxtIcerik=row?.findViewById(R.id.meicerik) as TextView
            //this.metxtTime=row?.findViewById(R.id.metime) as TextView
            this.txtKAdi=row?.findViewById(R.id.kAdi) as TextView
            this.txtIcerik=row?.findViewById(R.id.icerik) as TextView
            //this.txtTime=row?.findViewById(R.id.time) as TextView
            this.otherText=row?.findViewById(R.id.otherText) as LinearLayout
            this.meText=row?.findViewById(R.id.meText) as LinearLayout
        }

    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view:View?
        var viewHolder:ViewHolder
        if  (convertView==null){
            var layout=LayoutInflater.from(context)
            view=layout.inflate(R.layout.message_list,parent,false)
            viewHolder=ViewHolder(view)
            view.tag=viewHolder
        } else{
            view=convertView
            viewHolder=view.tag as ViewHolder
        }

        var mesajlar=getItem(position) as Mesajlar
        viewHolder.txtIcerik.text=mesajlar.Icerik
        viewHolder.txtKAdi.text=mesajlar.kAdi
        //  viewHolder.txtTime.text=mesajlar.Tarih.toString().substring(11,16)
        viewHolder.metxtIcerik.text=mesajlar.Icerik
        viewHolder.metxtKAdi.text=mesajlar.kAdi
        //viewHolder.metxtTime.text=mesajlar.Tarih.toString().substring(11,16)
        viewHolder.metxtIcerik.text=mesajlar.Icerik
        viewHolder.metxtKAdi.text=mesajlar.kAdi
        //viewHolder.metxtTime.text=mesajlar.Tarih.toString().substring(11,16)
        if  (mesajlar.kAdi.toString()!=_session.kAdi){
            viewHolder.meText.visibility=View.INVISIBLE
            viewHolder.otherText.visibility=View.VISIBLE

        } else{
            viewHolder.meText.visibility=View.VISIBLE
            viewHolder.otherText.visibility=View.INVISIBLE
        }


        return view as View
    }

    override fun getItem(position: Int): Any {
        return mesajlar.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mesajlar.count()
    }




}