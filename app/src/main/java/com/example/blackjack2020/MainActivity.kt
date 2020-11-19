package com.example.blackjack2020

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.blackjack2020.Interfaces.ISettingRepository
//import com.example.blackjack2020.interfaces.ISettingRepository
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
var totalFunds=25.0
class MainActivity : AppCompatActivity(), ISettingRepository {
    private lateinit var settingVar:ISettingRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingVar = SettingRepository()

        hp_play_btn.setOnClickListener{launchPlay()}
        hp_how_to_play_btn.setOnClickListener{launchHowToPlay()}
        hp_settings_btn.setOnClickListener{launchSettings()}
        hp_tips_btn.setOnClickListener{launchTipsNTricks()}

    }

    fun launchPlay() {
        if (totalFunds <5 ){
            Toast.makeText(this@MainActivity, "HA, you are poor... You cant play", Toast.LENGTH_SHORT).show()

        }else{
            val getSet = settingVar.getSetting(index)
            val json = Gson().toJson(getSet)
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra(LAUNCH_KEY, json)
            startActivityForResult(intent, SETTINGS_REQUEST_CODE)
            finish()
        }

    }

    fun launchHowToPlay(){
        val intent= Intent(this, HowToPlayActivity::class.java)
        startActivityForResult(intent,HOW_TO_PLAY_REQUEST_CODE)
    }
    fun launchSettings(){
        val getSet = settingVar.getSetting(index)
        val json = Gson().toJson(getSet)
        val intent= Intent(this, SettingsActivity::class.java)
        intent.putExtra(SET_KEY, json)
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
                                settingVar.editSettings(index, setting)
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
        val SETTINGS_REQUEST_CODE=1
        val TIPS_REQUEST_CODE=1
        val SET_KEY = "Setting Key"
        val LAUNCH_KEY= "Launch Key"
        val TAG = "Test"
        var index=0;



    }

    override fun getSettings(): List<SettingModel> {
        return settingVar.getSettings()
    }

    override fun getSetting(idx: Int): SettingModel {
        return settingVar.getSetting(idx)

    }

    override fun editSettings(idx: Int, setting: SettingModel) {
        //todo: Finish up the code once we implement it
    }

    override fun getCount(): Int {
        return settingVar.getCount()
    }
}