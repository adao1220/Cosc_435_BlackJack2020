package com.example.blackjack2020

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        set_return_btn.setOnClickListener (this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.set_return_btn ->{
                val intent = Intent()
                val ai = ""
                val card = ""
                val name = set_name_input.editableText.toString()

                try{
                    val setting = SettingModel(ai, card, name)
                    val json = Gson().toJson(setting)
                    intent.putExtra(SETTING_EXTRA_KEY,json)
                    setResult(Activity.RESULT_OK,intent)
                    finish()
                }catch(ex: Exception){
                    Toast.makeText(this, "Invalid somehow", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
    companion object{
        val SETTING_EXTRA_KEY = "SETTINGS"
    }
}