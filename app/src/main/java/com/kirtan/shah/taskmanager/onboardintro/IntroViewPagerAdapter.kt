package com.kirtan.shah.taskmanager.onboardintro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.kirtan.shah.taskmanager.R


class IntroViewPagerAdapter(context: Context,listscreen : List<ScreenItem>) : PagerAdapter() {

    var mContext : Context
    var mListScreen : List<ScreenItem>

    init {
        this.mContext = context
        this.mListScreen = listscreen
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        var myView : View = `object` as View
        container.removeView(myView)

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.layout_screen, null)

        val imgSlide: ImageView = layoutScreen.findViewById(R.id.intro_img)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_title)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_description)

        title.text = mListScreen[position].Title
        description.text = mListScreen[position].Description
        imgSlide.setImageResource(mListScreen[position].ScreenImg)

        container.addView(layoutScreen)

        return layoutScreen

    }
}