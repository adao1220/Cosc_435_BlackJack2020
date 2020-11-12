package com.example.blackjack2020

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.blackjack2020.MainActivity.Companion.SET_KEY
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val options = intent.getStringExtra(SET_KEY)
        if (options!= null){
            val toSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)

            when(toSet.difficulty){
                "set_ai_easy_btn" -> set_ai_easy_btn.isChecked = true
                "set_ai_normal_btn" -> set_ai_normal_btn.isChecked = true
                "set_ai_hard_btn" -> set_ai_hard_btn.isChecked = true
            }

            when(toSet.card){
                "cardface1" -> cardface1.isChecked = true
                "cardface2" -> cardface2.isChecked = true
                "cardface3" -> cardface3.isChecked = true
            }
            set_profile_name.setText(toSet.profileName)
            set_curr_funds.text = toSet.funds.toString()
            set_music_sw.isChecked = toSet.music
        }

        set_return_btn.setOnClickListener (this)
        set_add_funds.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.set_add_funds -> {
                var insertFunds = set_insert_funds.editableText.toString()
                val fundsAdd = insertFunds.toDouble()
                var currFund = set_curr_funds.text.toString()
                val currFunAdd = currFund.toDouble()
                val newFunds = fundsAdd + currFunAdd

                set_curr_funds.text = newFunds.toString()
                Toast.makeText(this, newFunds.toString(), Toast.LENGTH_SHORT).show()
                set_insert_funds.editableText.clear()


            }
            R.id.set_return_btn ->{
                val intent = Intent()
                var ai =""
                var card = "heh"
                when(aiGroup.checkedRadioButtonId){
                    R.id.set_ai_easy_btn-> {
                         ai = "set_ai_easy_btn"
                    }
                    R.id.set_ai_normal_btn-> {
                         ai = "set_ai_normal_btn"
                    }
                    R.id.set_ai_hard_btn-> {
                         ai = "set_ai_hard_btn"
                    }
                }
                when(cardGroup.checkedRadioButtonId){
                    R.id.cardface1-> {
                        card = "cardface1"
                    }
                    R.id.cardface2-> {
                        card = "cardface2"
                    }
                    R.id.cardface3-> {
                        card = "cardface3"
                    }
                }
                val name = set_profile_name.editableText.toString()
                val music = set_music_sw.isChecked
                val cash = set_curr_funds.text.toString()
                try{
                    val totalCash = cash.toDouble()
                    val setting = SettingModel(ai, card, name,totalCash, music)
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
        val TAG = "TESTING NOW"
    }
}