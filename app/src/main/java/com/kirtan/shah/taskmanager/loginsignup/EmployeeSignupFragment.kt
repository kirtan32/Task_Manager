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
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.employee.employeeActivity

class EmployeeSignupFragment : Fragment() {

    lateinit var email : TextInputEditText
    lateinit var pass : TextInputEditText
    lateinit var name : TextInputEditText
    lateinit var submit : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employee_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        email = view.findViewById(R.id.emailRegisterSignUp)
        pass = view.findViewById(R.id.passwordRegisterSignup)
        name = view.findViewById(R.id.nameRegisterSignUp)
        submit = view.findViewById(R.id.createAccountBtn)

        submit.setOnClickListener {
            val email = email.text.toString()
            val password = pass.text.toString()
            val name = name.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
            {
                LoginSignup.auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {

                    if (it.isSuccessful) {
                        // Apply insert employee data logic here
                        var UserData: User = User(name,email,password)
//                        LoginSignup.database.child("Employee").child("Username").get().addOnSuccessListener {admin->
//                            Log.i("firebase", "Got value ${admin.value}")
//                        }.addOnFailureListener{admin->
//                            Log.e("firebase", "Error getting data", admin)
//                        }
                        //LoginSignup.database.child("Employee").setValue(name)
                        LoginSignup.database.child("Employee").push().setValue(UserData).addOnCompleteListener {  datab->
                        //child(name).setValue(UserData).addOnCompleteListener { datab->
                            if(datab.isSuccessful)
                            {
                                Toast.makeText(view.context,"User Registered Successfully:)", Toast.LENGTH_LONG).show()
                            }
                            else
                            {
                                Toast.makeText(view.context,"Error in Registration", Toast.LENGTH_LONG).show()
                            }
                        }

//                        var playersRef = firebase.database().ref("players/");
//
//                        playersRef.set ({
//                            John: {
//                                number: 1,
//                                age: 30
//                        },
//
//                            Amanda: {
//                                number: 2,
//                                age: 20
//                        }
//                        });
                        //end

                    }

                }.addOnFailureListener {
                    Toast.makeText(context,it.localizedMessage, Toast.LENGTH_LONG).show()
                }

                LoginSignup.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {

                    if(it.isSuccessful){
                        Toast.makeText(view.context,"Welcome", Toast.LENGTH_LONG).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(view.context,it.localizedMessage, Toast.LENGTH_LONG).show()
                }

                //Setting Login Prefs
                setLoginPref(name,email)
                //End
                val myIntent = Intent(
                    activity?.applicationContext,
                    employeeActivity::class.java
                )
                activity?.startActivity(myIntent)
                activity?.finish()
            }
        }

    }
    fun restorePrefData(): Boolean {
        val pref = requireActivity().application.applicationContext.getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)
        if (pref != null) {
            return pref.getBoolean("isLogin", false)
        }
        return false
    }

    fun savePrefsData() {
        val pref = requireActivity().application.applicationContext.getSharedPreferences("myPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isLogin", true)
        editor.apply()
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
    class User{

        lateinit var name:String
        lateinit var user:String
        lateinit var pass:String

        //Default constructor required for calls to
        //DataSnapshot.getValue(User.class)
        constructor(){

        }

        constructor(name:String,email:String,ps:String){
            this.name=name
            this.user=email
            this.pass = ps
        }
    }
}

//@IgnoreExtraProperties
//data class User(val username: String? = null, val email: String? = null) {
//    // Null default values create a no-argument default constructor, which is needed
//    // for deserialization from a DataSnapshot.
//}
//class User{
//
//    lateinit var name:String
//    lateinit var user:String
//    lateinit var pass:String
//
//    //Default constructor required for calls to
//    //DataSnapshot.getValue(User.class)
//    constructor(){
//
//    }
//
//    constructor(name:String,email:String,ps:String){
//        this.name=name
//        this.user=email
//        this.pass = ps
//    }
//}