package com.kirtan.shah.taskmanager.onboardintro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.kirtan.shah.taskmanager.MainActivity
import com.kirtan.shah.taskmanager.R
import com.kirtan.shah.taskmanager.employee.employeeActivity
import com.kirtan.shah.taskmanager.loginsignup.LoginSignup
import org.w3c.dom.Text


class introActivity : AppCompatActivity()
{

    private lateinit var screenPager: ViewPager
    lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    lateinit var tabIndicator: TabLayout
    lateinit var btnNext: Button
    var position: Int = 0
    lateinit var btnGetStarted: Button
    lateinit var btnAnim: Animation
    lateinit var tvSkip: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // make the activity on full screen
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        // when this activity is about to be launch we need to check if its openened before or not


        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, LoginSignup::class.java)
            startActivity(mainActivity)
            finish()
        }

        setContentView(R.layout.activity_intro)

        // hide the action bar

        // hide the action bar
        //supportActionBar!!.hide()

        // ini views

        // ini views
        btnNext = findViewById<Button>(R.id.btn_next)
        btnGetStarted = findViewById<Button>(R.id.btn_get_started)
        tabIndicator = findViewById<TabLayout>(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        tvSkip = findViewById<TextView>(R.id.tv_skip)


        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(
            ScreenItem(
                "Tasker",
                "Manage your tasks and ideas Quickly",
                R.drawable.img1
            )
        )
        mList.add(
            ScreenItem(
                "Calender",
                "Track your Tasks with Calender",
                R.drawable.img2
            )
        )
        mList.add(
            ScreenItem(
                "Hello",
                "Welcome to The Task Manager",
                R.drawable.img3
            )
        )

        // setup viewpager

        // setup viewpager
        screenPager = findViewById<ViewPager>(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.setAdapter(introViewPagerAdapter)

        // setup tablayout with viewpager

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager)

        // next button click Listner

        // next button click Listner
        btnNext.setOnClickListener {
            position = screenPager.getCurrentItem();
            if (position < mList.size) {

                position++;
                screenPager.setCurrentItem(position);

            }

            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen();

            }
        }

        // tablayout add change listener

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var myTab=tab as TabLayout.Tab
                if (myTab!!.position == mList.size - 1) {
                    loaddLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        // Get Started button click listener


        // Get Started button click listener
        btnGetStarted.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                //open main activity
                val mainActivity = Intent(applicationContext, LoginSignup::class.java)
                startActivity(mainActivity)
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                savePrefsData()
                finish()
            }
        })

        // skip button click listener


        // skip button click listener
        tvSkip.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                screenPager.setCurrentItem(mList.size)
            }
        })

    }

    private fun restorePrefData(): Boolean {
        val pref =
            applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.apply()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE)
        btnGetStarted.setVisibility(View.VISIBLE)
        tvSkip.setVisibility(View.INVISIBLE)
        tabIndicator.setVisibility(View.INVISIBLE)
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim)
    }
}