package com.kirtan.shah.taskmanager.employee

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.android.material.button.MaterialButton
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.loginsignup.LoginSignup


class EmpProfileFragment : Fragment() {

    lateinit var signout : MaterialButton
    lateinit var empname : TextView
    lateinit var empemail : TextView
    lateinit var empid : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emp_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        empname = view.findViewById<TextView>(R.id.profilename)
        empemail = view.findViewById<TextView>(R.id.profileemail)
        empid = view.findViewById<TextView>(R.id.profileid)

        signout = view.findViewById(R.id.profilelogout)
        signout.setOnClickListener {

            setEmpLoginOut()
            val myIntent = Intent(
                activity?.applicationContext,
                LoginSignup::class.java
            )
            Toast.makeText(context, "Signed Out", Toast.LENGTH_LONG).show()
            activity?.startActivity(myIntent)
            activity?.finish()

        }

        empname.text=employeeActivity.empname
        empemail.text=employeeActivity.empemail
        empid.text=employeeActivity.empid
    }

    fun setEmpLoginOut() {
        val pref = requireActivity().application.applicationContext.getSharedPreferences(
            "myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        LoginSignup.auth.signOut()
        val editor = pref.edit()
        editor.putBoolean("isLogin", false)
        editor.putString("name", "")
        editor.putString("email", "")
        editor.putBoolean("isEmp", false)
        editor.putBoolean("isAdmin", false)
        editor.apply()
    }

}