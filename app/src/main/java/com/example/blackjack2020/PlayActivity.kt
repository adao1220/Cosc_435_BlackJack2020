package com.example.blackjack2020

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*

private var deck= CardsModel(CardRepository())

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        var difficulty=""
        var backCard=""
        val options = intent.getStringExtra(MainActivity.LAUNCH_KEY)
        if (options!= null){
            val toSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            difficulty = toSet.difficulty
            //ToDo: need to do the decimal stuff
            play_cash.text = "Total Cash: $" + toSet.funds.toString()
            backCard = toSet.card
        }



        play_hit_btn.setOnClickListener { hit("user")  }
        deal()
        play_new_game_btn.setOnClickListener{confirm()}
        play_stand_btn.setOnClickListener{stand(difficulty)}




    }

    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {
            //******USER********
            var card1 = deck.getRandomCard()
            deck.addToHand(card1,"user")
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            deck.addToHand(card2,"user")
            userCount += deck.getValue(card2)
            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            deck.addToHand(card1,"dealer")
            dealerCount += deck.getValue(card2)

            card2 = deck.getRandomCard()
            deck.addToHand(card2,"dealer")
            dealerCount += deck.getValue(card2)

            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)
        }

    }

    fun hit(string: String){ //will allow a new card unless over score of 21
        if(string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                userCount += deck.getValue(newCard)
                play_score.text= userCount.toString()

                var message = "Your new card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)
            } else
                Log.d(tag, "You've already lost")
        }
        else{
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                dealerCount += deck.getValue(newCard)

                var message = "Dealers card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)

        }

    }


    fun reset(){
        userCount=0
        dealerCount=0
        gameover=false
        deck.newGame()
        deal()
    }




    fun stand(difficultyString:String){

        Log.d(tag, "User Finished with score of: "+ userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: "+ dealerCount)
        //Log.d(tag, "Dealer cards are: " + deck.getHand("dealer"))
        difficultyAI(difficultyString)
        gameover=true
    }

    fun difficultyAI(level: String)
    {
        when(level){
            "set_ai_easy_btn"->{
                    while (dealerCount<=12)
                        hit("dealer")
                    if(dealerCount<=21 && userCount>21)
                        Log.d(tag, "Dealer won, user went over 21 ")
                    if(userCount<=21 && dealerCount>21)
                        Log.d(tag, "User won, dealer went over 21 ")
                    else if ((dealerCount> userCount)&& (dealerCount<=21))
                        Log.d(tag, "Dealer won with score of: "+ dealerCount)
                    else if ((dealerCount< userCount)&& (userCount<=21))
                        Log.d(tag, "User won with score of: "+ userCount)
                    else if((dealerCount== userCount))
                        Log.d(tag, "It's a tie")
                Log.d(tag, "Easy")
            }
            "set_ai_normal_btn"->{
                while (dealerCount<=12)
                    hit("dealer")
                if(dealerCount<=21 && userCount>21)
                    Log.d(tag, "Dealer won, user went over 21 ")
                if(userCount<=21 && dealerCount>21)
                    Log.d(tag, "User won, dealer went over 21 ")
                else if ((dealerCount> userCount)&& (dealerCount<=21))
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                else if ((dealerCount< userCount)&& (userCount<=21))
                    Log.d(tag, "User won with score of: "+ userCount)
                else if((dealerCount== userCount))
                    Log.d(tag, "It's a tie")
                Log.d(tag, "Normal")
                //Todo Make normal mode

            }
            "set_ai_hard_btn"->{
                while (dealerCount<=12)
                    hit("dealer")
                if(dealerCount<=21 && userCount>21)
                    Log.d(tag, "Dealer won, user went over 21 ")
                if(userCount<=21 && dealerCount>21)
                    Log.d(tag, "User won, dealer went over 21 ")
                else if ((dealerCount> userCount)&& (dealerCount<=21))
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                else if ((dealerCount< userCount)&& (userCount<=21))
                    Log.d(tag, "User won with score of: "+ userCount)
                else if((dealerCount== userCount))
                    Log.d(tag, "It's a tie")
                Log.d(tag, "Hard")
                //Todo Make hard mode
            }
        }


    }

    fun confirm()
    {
        when(!gameover){
            true->{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to start a new game?");
                builder.setTitle("New Game!")
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { dialog, which -> reset() }
                builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show();
            }
            false->{
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