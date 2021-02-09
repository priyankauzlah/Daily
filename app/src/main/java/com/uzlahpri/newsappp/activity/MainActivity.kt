package com.uzlahpri.newsappp.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.uzlahpri.newsappp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun getLaunchService(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        ib_profile_main.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.ib_profile_main -> startActivity(Intent(ProfileActivity.getLaunchService(this)))
        }
    }
}