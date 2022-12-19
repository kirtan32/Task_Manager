package com.kirtan.shah.taskmanager.loginsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.admin.adminActivity
import com.kirtan.shah.taskmanager.employee.employeeActivity

class AdminLoginFragment : Fragment() {

    var TAG = "Admin Login"
    lateinit var email: TextInputEditText
    lateinit var pass: TextInputEditText
    lateinit var submit: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        email = view.findViewById(R.id.emailAdminLogin)
        pass = view.findViewById(R.id.passwordAdminLogin)
        submit = view.findViewById(R.id.adminloginBtn)

        submit.setOnClickListener {
            val email = email.text.toString()
            val password = pass.text.toString()

            var adminemail: String = "admin"
            var adminpass: String = "admin"

            if (email.isNotEmpty() && password.isNotEmpty()) {

                getDataFromFirebase()
                // getting Admin Username
                /*
                LoginSignup.database.child("Admin").child("Username").get()
                    .addOnSuccessListener { admin ->
                        Log.i("firebase", "Got value ${admin.value}")
                        adminemail = admin.value.toString()
                    }.addOnFailureListener { admin ->
                    Log.e("firebase", "Error getting data", admin)
                }

                //Getting Admin Password
                LoginSignup.database.child("Admin").child("Password").get()
                    .addOnSuccessListener { admin ->
                        Log.i("firebase", "Got value ${admin.value}")
                        adminpass = admin.value.toString()
                    }.addOnFailureListener { admin ->
                    Log.e("firebase", "Error getting data", admin)
                }*/

                //Sign In Procedure

                // Setting Login Prefs
                //Toast.makeText(context, "${email},${adminemail},${password},${adminpass}", Toast.LENGTH_LONG).show()

                if((email == adminemail) && (password == adminpass))
                {
                    //Toast.makeText(context, "${email},${adminemail},${password},${adminpass}", Toast.LENGTH_LONG).show()
                    setAdminLoginPref(email, email)
                    val myIntent = Intent(
                        activity?.applicationContext,
                        adminActivity::class.java
                    )
                    Toast.makeText(context, "Welcome!! Admin", Toast.LENGTH_LONG).show()
                    activity?.startActivity(myIntent)
                    activity?.finish()
                }
                else
                {
                    Toast.makeText(context, "Username or Password Incorrect", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    fun setAdminLoginPref(name: String, email: String) {
        val pref = requireActivity().application.applicationContext.getSharedPreferences(
            "myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putBoolean("isEmp", false)
        editor.putBoolean("isAdmin", true)
        editor.apply()
    }

    fun checkIfLogin(): Boolean {
        val pref = requireActivity().application.applicationContext.getSharedPreferences(
            "myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        return pref.getBoolean("isLogin", false)

    }

    fun getDataFromFirebase()
    {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                var adminemail = dataSnapshot.child("Username").getValue().toString()
                var adminpass =  dataSnapshot.child("Password").getValue().toString()
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        LoginSignup.database.child("Admin").addValueEventListener(postListener)

    }
}