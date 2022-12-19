package com.kirtan.shah.taskmanager.loginsignup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.employee.employeeActivity

class EmployeeLoginFragment : Fragment() {

    lateinit var email : TextInputEditText
    lateinit var pass : TextInputEditText
    lateinit var submit : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_employee_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        email = view.findViewById(R.id.emailLogin)
        pass = view.findViewById(R.id.passwordLogin)
        submit = view.findViewById(R.id.loginBtn)

        submit.setOnClickListener {
            val email = email.text.toString()
            val password = pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                LoginSignup.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            val myIntent = Intent(
                                activity?.applicationContext,
                                employeeActivity::class.java
                            )
                            // Setting Login Prefs
                            setLoginPref(email,email)

                            Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
                            activity?.startActivity(myIntent)
                            activity?.finish()

                        }

                    }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun setLoginPref(name: String,email: String)
    {
        val pref = requireActivity().application.applicationContext.getSharedPreferences("myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putBoolean("isEmp",true)
        editor.putBoolean("isAdmin",false)
        editor.apply()
    }
    fun checkIfLogin() : Boolean
    {
        val pref = requireActivity().application.applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isLogin", false)

    }
}
