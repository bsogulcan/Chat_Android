package com.suat.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_profil.*
import org.w3c.dom.Text
import java.io.Serializable


class ProfilFragment: Fragment() {
        private lateinit var myObject: Session
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                myObject = arguments!!.getSerializable(ARG_PARAM) as Session
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lblKullanici.setText(myObject.kAdi)
    }

    companion object {
            private val ARG_PARAM:String? = "myObject"

            fun newInstance(_session: Session): ProfilFragment {
                val fragment = ProfilFragment()
                val args = Bundle()
                args.putSerializable(ARG_PARAM, _session as Serializable)
                fragment.arguments = args
                return fragment
            }
        }
    }
