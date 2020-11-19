package com.example.blackjack2020

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.blackjack2020.models.Card
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.settings_frag.*
import kotlinx.android.synthetic.main.settings_frag.set_ai_easy_btn
import kotlinx.android.synthetic.main.settings_frag.set_ai_hard_btn
import kotlinx.android.synthetic.main.settings_frag.set_ai_normal_btn
import kotlinx.android.synthetic.main.settings_frag.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class Play_Settings_fragment : Fragment() {

    companion object {
        val cardface = "cardface"
        val difficulty = "difficulty"
        val funds = "funds"
        val music = "music"
        val name = "name"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.settings_frag, container, false)
        val cardface = arguments?.getString("cardface")
        val difficulty = arguments?.getString("difficulty")
        val funds = arguments?.getString("funds")
        val name = arguments?.getString("name")
        val music = arguments?.getString("music")


        when (cardface) {
            "cardface1" -> view.cardface1.isChecked = true
            "cardface2" -> view.cardface2.isChecked = true
            "cardface3" -> view.cardface3.isChecked = true
        }

        when (difficulty) {
            "set_ai_easy_btn" -> view.set_ai_easy_btn.isChecked = true
            "set_ai_normal_btn" -> view.set_ai_normal_btn.isChecked = true
            "set_ai_hard_btn" -> view.set_ai_hard_btn.isChecked = true
        }
        view.set_curr_funds.text = totalFunds.toString()
        //view.cardface2.isChecked=true
        view.frag_Apply.setOnClickListener { onApply(view) }
        view.set_add_funds.setOnClickListener { addmoney(view) }


        return view
    }


    fun addmoney(view: View?) {
        var insertFunds = view!!.set_insert_funds.editableText.toString()
        val fundsAdd = insertFunds.toDouble()
        var currFund = view.set_curr_funds.text.toString()
        val currFunAdd = currFund.toDouble()
        val newFunds = fundsAdd + currFunAdd

        view.set_curr_funds.text = newFunds.toString()
        view.set_insert_funds.setText("0")
        totalFunds = newFunds
        Log.d(PlayActivity.tag, "Total funds:  " + totalFunds)

        var textview: TextView
        textview=activity!!.findViewById(R.id.play_cash)

        textview.text="Total Cash: $" + totalFunds.toString()

    }


    fun onApply(view: View?) {
        var ai = ""
        var card = ""
        when (view!!.aiGroup.checkedRadioButtonId) {
            R.id.set_ai_easy_btn -> {
                ai = "set_ai_easy_btn"
            }
            R.id.set_ai_normal_btn -> {
                ai = "set_ai_normal_btn"
            }
            R.id.set_ai_hard_btn -> {
                ai = "set_ai_hard_btn"
            }
        }
        when (view.cardGroup.checkedRadioButtonId) {
            R.id.cardface1 -> {
                card = "cardface1"
            }
            R.id.cardface2 -> {
                card = "cardface2"
            }
            R.id.cardface3 -> {
                card = "cardface3"
            }
        }
//                val name = set_profile_name.editableText.toString()
//                val music = view.set_music_sw.isChecked
        val cash = view.set_curr_funds.text.toString()
        try {
            val totalCash = cash.toDouble()
            totalFunds = totalCash
            PlayActivity.backCard = card
            PlayActivity.difficulty = ai
            PlayActivity.name = name
            PlayActivity.music = music.toBoolean()
            PlayActivity.max= totalFunds.toInt()


        } catch (ex: Exception) {

        }
        //getFragmentManager().!!popBackStack();

        var temp: ImageView
        temp=activity!!.findViewById(R.id.dealer_card_2)
        when(card){
            "cardface1"->{
                temp.setImageResource(R.drawable.card_face_1)
            }
            "cardface2"->{
                temp.setImageResource(R.drawable.card_face_2)                }
            "cardface3"->{
                temp.setImageResource(R.drawable.card_face_3)                }
        }


        //var betview: TextView
        var betbarview: SeekBar
        //betview=activity!!.findViewById(R.id.play_current_bet)
        betbarview=activity!!.findViewById(R.id.play_betbar)

        betbarview!!.max = (PlayActivity.max - PlayActivity.min)/ PlayActivity.step
        //betview!!.text = "Current Bet: $${PlayActivity.min}"
        var playhitbtn : Button
        playhitbtn=activity!!.findViewById(R.id.play_hit_btn)
        var playstandbtn : Button
        playstandbtn=activity!!.findViewById(R.id.play_stand_btn)
        var playnewgamebtn : Button
        playnewgamebtn=activity!!.findViewById(R.id.play_new_game_btn)

        if(totalFunds>=5) {
            playhitbtn.isClickable = true
            playstandbtn.isClickable = true
            playnewgamebtn.isClickable = true
        }

        getFragmentManager()!!.beginTransaction().remove(this).commit()
    }
}




