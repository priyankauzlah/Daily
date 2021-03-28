package com.uzlahpri.newsappp.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.uzlahpri.newsappp.NewsAdapter
import com.uzlahpri.newsappp.R
import com.uzlahpri.newsappp.model.ResponseNews
import com.uzlahpri.newsappp.service.RetrofitConfig
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    val date = getCurrentDateTime()

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

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
        tv_date_main.text = date.toString("dd/MM/yy")
        getNews()

//        //harusnya dihapus
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//        refUsers = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
//        refUsers!!.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (p0 in snapshot.children) {
//                    val photo = snapshot.child("photo").value.toString()
//                    Glide.with(this@MainActivity).load(photo).into(iv_profile_main)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//        //sampe sini dihapus
    }

    private fun getNews() {
        var country = "id"
        var apiKey = "5300eb7137d64dba81f6ab851ebe3bd7"

        var loading = ProgressDialog.show(this, "Request data", "Loading...")
        RetrofitConfig.getInstance().getNewsData(country, apiKey).enqueue(
            object : retrofit2.Callback<ResponseNews> {
                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ) {
                    Log.d("Response", "Succes" + response.body()?.articles)
                    loading.dismiss()
                    if (response.isSuccessful) {
                        val status = response.body()?.status
                        if (status.equals("ok")) {
                            Toast.makeText(this@MainActivity, "Data Success !", Toast.LENGTH_SHORT)
                                .show()
                            val newsData = response.body()?.articles
                            val newsDatImage = response.body()!!
                            Glide.with(this@MainActivity)
                                .load(newsDatImage.articles?.component5()?.urlToImage).centerCrop()
                                .into(iv_main_banner)
                            tv_highlight.text =
                                newsDatImage.articles?.component5()?.title.toString()
                            tv_name_author.text =
                                newsDatImage.articles?.component5()?.author.toString()

                            val newsAdapter = NewsAdapter(this@MainActivity, newsData)
                            rv_main.adapter = newsAdapter
                            rv_main.layoutManager = LinearLayoutManager(this@MainActivity)

                            val dataHighlight = response.body()
                            Glide.with(this@MainActivity)
                                .load(dataHighlight?.articles?.component5()?.urlToImage)
                                .centerCrop().into(iv_main_banner)

                            tv_highlight.text = dataHighlight?.articles?.component5()?.title
                            tv_name_author.text = dataHighlight?.articles?.component5()?.author
                            tv_date_highlight.text =
                                dataHighlight?.articles?.component5()?.publishedAt

                        } else {
                            Toast.makeText(this@MainActivity, "Data Failed !", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    Log.d("Response", "Failed : " + t.localizedMessage)
                    loading.dismiss()
                }

            }
        )
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.ib_profile_main -> startActivity(Intent(ProfileActivity.getLaunchService(this)))
            R.id.card_image -> Toast.makeText(this@MainActivity, "This is a Highlight !", Toast.LENGTH_SHORT).show()
        }
    }
}