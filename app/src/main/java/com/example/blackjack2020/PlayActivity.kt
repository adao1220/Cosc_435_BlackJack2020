package com.example.blackjack2020

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*

private var deck= CardsModel(CardRepository())

class PlayActivity : AppCompatActivity() {
    private var BetView: TextView? = null
    private var BetBarView: SeekBar? = null

    private var min = 5
    private var max = 0
    private var step = 5
    private var currentBet = 5
    private var newBalance = 0.0

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)


        var difficulty=""
        var backCard=""

        val options = intent.getStringExtra(MainActivity.LAUNCH_KEY)
        if (options!= null){
            val FromSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            difficulty = FromSet.difficulty
            //ToDo: need to do the decimal stuff
            totalFunds = FromSet.funds
            newBalance = totalFunds
            play_cash.text = "Total Cash: $" + totalFunds.toString()
            backCard = FromSet.card
            max = totalFunds.toInt()
        }
        deal()
        // Todo: The betting bar code

        BetView = this.play_current_bet
        BetBarView = this.play_betbar
        BetBarView!!.max = (max -min)/ step
        BetView!!.text = "Current Bet: $$min"

        BetBarView?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                currentBet = min + (progress * step)
                BetView!!.text = "Current Bet: $$currentBet"

            }

            override fun onStartTrackingTouch(seek: SeekBar) {}
            override fun onStopTrackingTouch(seek: SeekBar) {}
        })

        //Todo refactor total funds
        play_hit_btn.setOnClickListener { hit("user", currentBet)  }
        play_new_game_btn.setOnClickListener{confirm()}
        play_stand_btn.setOnClickListener{stand(difficulty, currentBet)}





    }

    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {
            //******USER********
            var card1 = deck.getRandomCard()
            deck.addToHand(card1, "user")
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            deck.addToHand(card2, "user")
            userCount += deck.getValue(card2)
            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            deck.addToHand(card1, "dealer")
            dealerCount += deck.getValue(card2)

            card2 = deck.getRandomCard()
            deck.addToHand(card2, "dealer")
            dealerCount += deck.getValue(card2)

            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)
        }

    }

    fun hit(string: String, currentBet: Int){ //will allow a new card unless over score of 21
        if(string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                userCount += deck.getValue(newCard)
                play_score.text= userCount.toString()

                var message = "Your new card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)
            } else{
                Log.d(tag, "You've already lost")
                play_hit_btn.isClickable = false
                play_stand_btn.isClickable = false
                lostBet(currentBet)

            }
        }
        else{
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                dealerCount += deck.getValue(newCard)
                var message = "Dealers card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)

        }

    }


    fun dealerHit(string: String){
        var newCard = deck.getRandomCard()
        deck.addToHand(newCard, string)
        dealerCount += deck.getValue(newCard)

        var message = "Dealers card is: " + deck.cardFormat(newCard)
        Log.d(tag, message)
    }






    fun stand(difficultyString: String, currentBet: Int){

        Log.d(tag, "User Finished with score of: " + userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: " + dealerCount)
        play_hit_btn.isClickable = false
        play_stand_btn.isClickable = false
        val winLose = difficultyAI(difficultyString)
        when (winLose){
            0 -> wonBet(currentBet)
            1 -> lostBet(currentBet)

        }
        gameover=true
    }

    private fun lostBet(currentBet: Int){
        var newFun = totalFunds
        totalFunds = newFun - currentBet
        play_cash.text = "Total Cash: $" + totalFunds.toString()
    }

    fun wonBet(currentBet: Int){
        var newFun = totalFunds
        totalFunds = newFun + currentBet
        play_cash.text = "Total Cash: $" + totalFunds.toString()
    }




    fun difficultyAI(level: String): Int{
        when(level){
            "set_ai_easy_btn" -> {
                while (dealerCount <= 12)
                    dealerHit("dealer")
                if (dealerCount <= 21 && userCount > 21) {
                    Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if (userCount <= 21 && dealerCount > 21) {
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                } else if ((dealerCount > userCount) && (dealerCount <= 21)) {
                    Log.d(tag, "Dealer won with score of: " + dealerCount)
                    return 1
                } else if ((dealerCount < userCount) && (userCount <= 21)) {
                    Log.d(tag, "User won with score of: " + userCount)
                    return 0
                } else if ((dealerCount == userCount)) {
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Easy")
            }
            "set_ai_normal_btn" -> {
                while (dealerCount <= 12)
                    dealerHit("dealer")
                if (dealerCount <= 21 && userCount > 21)
                    Log.d(tag, "Dealer won, user went over 21 ")
                if (userCount <= 21 && dealerCount > 21)
                    Log.d(tag, "User won, dealer went over 21 ")
                else if ((dealerCount > userCount) && (dealerCount <= 21))
                    Log.d(tag, "Dealer won with score of: " + dealerCount)
                else if ((dealerCount < userCount) && (userCount <= 21))
                    Log.d(tag, "User won with score of: " + userCount)
                else if ((dealerCount == userCount))
                    Log.d(tag, "It's a tie")
                Log.d(tag, "Normal")
                //Todo Make normal mode

            }
            "set_ai_hard_btn" -> {
                while (dealerCount <= 12)
                    dealerHit("dealer")
                if (dealerCount <= 21 && userCount > 21)
                    Log.d(tag, "Dealer won, user went over 21 ")
                if (userCount <= 21 && dealerCount > 21)
                    Log.d(tag, "User won, dealer went over 21 ")
                else if ((dealerCount > userCount) && (dealerCount <= 21))
                    Log.d(tag, "Dealer won with score of: " + dealerCount)
                else if ((dealerCount < userCount) && (userCount <= 21))
                    Log.d(tag, "User won with score of: " + userCount)
                else if ((dealerCount == userCount))
                    Log.d(tag, "It's a tie")
                Log.d(tag, "Hard")
                //Todo Make hard mode
            }
        }
    return 3
    }
    fun reset(){
        userCount=0
        play_hit_btn.isClickable = true
        play_stand_btn.isClickable = true
        dealerCount=0
        gameover=false
        deck.newGame()
        max = totalFunds.toInt()

        Toast.makeText(this@PlayActivity, "Reset Cash: " + max.toString(), Toast.LENGTH_SHORT).show()
        BetBarView!!.max = (max -min)/ step
        saveData();
        deal()
    }

    fun saveData(){
        val options = intent.getStringExtra(MainActivity.LAUNCH_KEY)
        if (options!= null){
            val FromSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            val difficulty = FromSet.difficulty
            val card = FromSet.card
            val name = FromSet.profileName
            totalFunds
            val music = true


            val settings = SettingModel(difficulty, card, name, totalFunds, music)
            SettingRepository().editSettings(0, settings)
        }
    }
    fun confirm()
    {
        when(!gameover){
            true -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to start a new game?");
                builder.setTitle("New Game!")
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { dialog, which -> reset() }
                builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show();
            }
            false -> {
                reset()
            }
        }


    }

    companion object{
        const val tag="test"
        var userCount=0 // holds score of user
        var dealerCount=0 //holds dealers score
        var gameover = false
    }


//    Old Code
//    fun score(){
//        var message= "Your Score is : "+ userCount
//        Log.d(tag, message)
//    }
//    fun myhand(){
//        deck.getHand("user")
//    }
}