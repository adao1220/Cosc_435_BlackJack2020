package com.example.blackjack2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

private const val NUM_PAGES = 3

class HowToPlayActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        viewPager = findViewById(R.id.pager)

        val pagerAdapter = PagerAdapter(this)
        viewPager.adapter = pagerAdapter


//        val pAdapter = PagerAdapter(supportFragmentManager)
//        pAdapter.addfragment(FragmentOneHtpOne(), "Goal")
//        pAdapter.addfragment(FragmentTwoHtp(), "Rules")
//        viewPager.adapter = pAdapter

    }
    override fun onBackPressed() {
        if(viewPager.currentItem == 0){
            super.onBackPressed()
        }else{
            viewPager.currentItem = viewPager.currentItem - 1
        }

    }
    private inner class PagerAdapter(manager: FragmentActivity) : FragmentStateAdapter(manager) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return FragmentOneHtpOne()
                1 -> return FragmentTwoHtp()
                else -> {
                    return FragmentThreeHtp()
                }
            }
        }
    }
}