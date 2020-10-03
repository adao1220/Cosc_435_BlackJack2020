package com.example.blackjack2020

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
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
    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SETTINGS_REQUEST_CODE ->{
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        val json = data?.getStringExtra(SettingsActivity.SETTING_EXTRA_KEY)
                        when(json){
                            null->return
                            else->{
                                val setting = Gson().fromJson(json, SettingModel::class.java)
                                Log.i(TAG, setting.toString())
                            }
                        }
                    }
                }
            }
            //from here, we can copy and paste from "SETTING_REQUEST_CODE ->{ " line and do it for the other pages
        }
    }




    companion object{
        val HOW_TO_PLAY_REQUEST_CODE=1
        val PLAY_REQUEST_CODE=1
        val SETTINGS_REQUEST_CODE=1
        val TIPS_REQUEST_CODE=1
        val TAG = "Test"


    }
}