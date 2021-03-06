package com.example.blackjack2020

import android.app.*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.blackjack2020.Interfaces.ISettingRepository
import com.example.blackjack2020.SettingsActivity.Companion.ProfileName
import com.example.blackjack2020.SettingsActivity.Companion.TotalFunds
//import com.example.blackjack2020.interfaces.ISettingRepository
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity(), ISettingRepository {
    private lateinit var settingVar:ISettingRepository

    //notification start

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "com.example.blackjack2020"
    private val description = "Test notification"

    //notification end
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingVar = SettingRepository()
        if(load == 1){
            load = 0
            finish()
            startActivity(getIntent())
        }

        hp_play_btn.setOnClickListener{launchPlay()}
        hp_how_to_play_btn.setOnClickListener{launchHowToPlay()}
        hp_settings_btn.setOnClickListener{launchSettings()}
        hp_tips_btn.setOnClickListener{launchTipsNTricks()}

    }
//
//    fun notification(string:String){
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val intent = Intent(this,SettingsActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
//            notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.GREEN
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//            builder = Notification.Builder(this,channelId)
//                .setContentTitle(string)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentIntent(pendingIntent)
//        }else{
//
//            builder = Notification.Builder(this)
//                .setContentTitle(string)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentIntent(pendingIntent)
//        }
//        notificationManager.notify(1234,builder.build())
//
//    }

    fun launchPlay() {
        if (TotalFunds <5 ){
            if (ProfileName==""){
                startService(Intent(this, NotificationServices::class.java))
            }
            else{
                Toast.makeText(this@MainActivity, "Add more money: Go to Settings", Toast.LENGTH_SHORT).show()
            }
        }
        else{
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
        val LOGIN_KEY = "Login"
        val FUNDS_KEY = "funds"
        val TAG = "Test"
        var index=0;
        var load = 0;


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