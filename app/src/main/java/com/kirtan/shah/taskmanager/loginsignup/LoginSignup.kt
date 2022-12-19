package com.kirtan.shah.taskmanager.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.admin.adminActivity
import com.kirtan.shah.taskmanager.employee.EmployeeCalenderFragment
import com.kirtan.shah.taskmanager.employee.EmployeeHomeFragment
import com.kirtan.shah.taskmanager.employee.employeeActivity

class LoginSignup : AppCompatActivity() {

    lateinit var navigationView: BottomNavigationView
    companion object{
        lateinit var auth: FirebaseAuth
        lateinit var database: DatabaseReference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        if(checkIfLogin())
        {
            if(checkifEmp())
            {
                val act = Intent(applicationContext, employeeActivity::class.java)
                startActivity(act)
                finish()
            }
            else if(checkifAdmin())
            {
                val act = Intent(applicationContext, adminActivity::class.java)
                startActivity(act)
                finish()
            }
        }
        //Firebase Authentication instance
        auth= FirebaseAuth.getInstance()
        database = Firebase.database.reference

        navigationView = findViewById<BottomNavigationView>(R.id.bottomLogin_navigation)
        supportFragmentManager.beginTransaction().replace(R.id.bodyLogin_container, EmployeeLoginFragment())
            .commit()
        navigationView.setSelectedItemId(R.id.nav_login)

        navigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_login -> fragment = EmployeeLoginFragment()
                R.id.nav_signup -> fragment = EmployeeSignupFragment()
                R.id.nav_adminlogin -> fragment = AdminLoginFragment()
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.bodyLogin_container, fragment)
                    .commit()
            }
            true
        })

    }

    fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        if (pref != null) {
            return pref.getBoolean("isLogin", false)
        }
        return false
    }

    fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.apply()
    }

    fun setLoginPref(name: String,email: String)
    {
        val pref = applicationContext.getSharedPreferences("myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.putString("name",name)
        editor.putString("email",email)
        editor.apply()
    }
    fun checkIfLogin() : Boolean
    {
        val pref = applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isLogin", false)

    }
    fun checkifEmp():Boolean
    {
        val pref = applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isEmp", false)
    }
    fun checkifAdmin():Boolean
    {
        val pref = applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isAdmin", false)
    }

}