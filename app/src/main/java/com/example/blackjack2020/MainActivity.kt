package com.example.blackjack2020

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hp_play_btn.setOnClickListener{launchPlay()}
        hp_how_to_play_btn.setOnClickListener{launchHowToPlay()}
        hp_settings_btn.setOnClickListener{launchSettings()}
        hp_tips_btn.setOnClickListener{launchTipsNTricks()}

    }

    fun launchPlay() {
        val intent = Intent(this, PlayActivity::class.java)
        startActivityForResult(intent, PLAY_REQUEST_CODE)
    }

    fun launchHowToPlay(){
        val intent= Intent(this, HowToPlayActivity::class.java)
        startActivityForResult(intent,HOW_TO_PLAY_REQUEST_CODE)
    }
    fun launchSettings(){
        val intent= Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }
    fun launchTipsNTricks(){
        val intent= Intent(this, TipsNTricksActivity::class.java)
        startActivityForResult(intent, TIPS_REQUEST_CODE)
    }








    companion object{
        val HOW_TO_PLAY_REQUEST_CODE=1
        val PLAY_REQUEST_CODE=1
        val SETTINGS_REQUEST_CODE=1
        val TIPS_REQUEST_CODE=1



    }
}