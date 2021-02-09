package com.uzlahpri.newsappp.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.uzlahpri.newsappp.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId: String = ""

    companion object {
        fun getLaunchService(from: Context) = Intent(from, SignUpActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        ib_back_signup.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.ib_back_signup -> startActivity(Intent(SignInActivity.getLaunchService(this)))
            R.id.btn_signup -> signUpUser()
        }
    }

    private fun signUpUser() {
        val fullName: String = et_fullname.text.toString()
        val email: String = et_email.text.toString()
        val password: String = et_password.text.toString()
        val confirmPassword: String = et_confirm_password.text.toString()

        if (fullName == "") {
            Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show()
        } else if (email == "") {
            Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show()
        } else if (password == "") {
            Toast.makeText(this, getString(R.string.error_text), Toast.LENGTH_SHORT).show()
        } else if ((confirmPassword == "").equals(password)) {
            Toast.makeText(this, getString(R.string.error_pass), Toast.LENGTH_SHORT).show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    firebaseUserId = mAuth.currentUser!!.uid
                    refUsers =
                        FirebaseDatabase.getInstance().reference.child(getString(R.string.text_user))
                            .child(firebaseUserId)
                    val userHashMap = HashMap<String, Any>()

                    // harus berurutan sama di Users (yg di packcage model)
                    userHashMap["uid"] = firebaseUserId
                    userHashMap["fullName"] = fullName
                    userHashMap["email"] = email
                    userHashMap["linkedIn"] = ""
                    userHashMap["instagram"] = ""
                    userHashMap["medium"] = ""
                    userHashMap["photo"] = ""

                    refUsers.updateChildren(userHashMap).addOnCompleteListener { it ->
                        if (it.isSuccessful) {
//                            startActivity(Intent(MainActivity.getLaunchService(this)))
//                            finish()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_register) + it.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}