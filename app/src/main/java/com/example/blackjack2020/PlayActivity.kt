package com.example.blackjack2020

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.blackjack2020.models.Card
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*

private var deck= CardsModel(CardRepository())
const val USER="user"
const val DEALER="dealer"
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
            backCard = toSet.card
        }


        reset()
        play_hit_btn.setOnClickListener { hit(USER)  }
        deal()
        play_new_game_btn.setOnClickListener{confirm()}
        play_stand_btn.setOnClickListener{stand(difficulty)}




    }

    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {
            //******USER********
            var card1 = deck.getRandomCard()
            deck.addToHand(card1,USER)
            isAce(USER, card1)
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            isAce(USER, card2)
            deck.addToHand(card2,USER)

            userCount += deck.getValue(card2)
            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + cardFormat(card1) + " and " + cardFormat(card2)
            Log.d(tag, message)


            //******DEALER***********
            card1 = deck.getRandomCard()
            isAce(DEALER, card1)
            deck.addToHand(card1,DEALER)

            dealerCount += deck.getValue(card1)
            card2 = deck.getRandomCard()
            isAce(DEALER, card2)
            deck.addToHand(card2,DEALER)
            dealerCount += deck.getValue(card2)
            message =
                "Dealers cards are: " + cardFormat(card1) + " and " + cardFormat(card2)
            Log.d(tag, message)
        }

    }


    fun hit(role: String){
        when(role){
            USER->{
                when(userCount){
                    in 1..21->{ //will allow a new card unless over score of 21
                        var newCard = deck.getRandomCard()
                        isAce(USER, newCard)
                        deck.addToHand(newCard, USER)

                        userCount += deck.getValue(newCard)
                        if(userCount>21 && userNumAces>0) //checks for aces
                        {
                            userCount-=10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                            Log.d(tag, "TREATING ACE AS 1")
                            userNumAces--
                        }
                        play_score.text= userCount.toString()
                        var message = "Your new card is: " + cardFormat(newCard)
                        Log.d(tag, message)
                    }
                    else->{
                        Log.d(tag, "You already lost")
                    }
                }
            }
            DEALER->{
                when(dealerCount){
                    in 1..21->{
                        var newCard = deck.getRandomCard()
                        isAce(DEALER, newCard) // increments variable *role*numAces
                        deck.addToHand(newCard, DEALER)
                        dealerCount += deck.getValue(newCard)
                        if(dealerCount>21 && dealerNumAces>0) //checks for aces
                        {
                            dealerCount-=10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                            Log.d(tag, "TREATING ACE AS 1")
                            dealerNumAces--
                        }

                        var message = "Dealers card is: " + cardFormat(newCard)
                        Log.d(tag, message)
                    }
                    else->{
                        Log.d(tag, "You already lost")

                    }
                }
            }

        }

    }




    fun reset(){
        play_hit_btn.isClickable=true
        play_stand_btn.isClickable=true
        userCount=0
        dealerCount=0
        userNumAces=0
        dealerNumAces=0
        gameover=false
        deck.newGame()
        deal()
    }




    fun stand(difficultyString:String){
        Log.d(tag, "User Finished with score of: "+ userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: "+ dealerCount)
        difficultyAI(difficultyString)
        play_hit_btn.isClickable=false
        play_stand_btn.isClickable=false
        gameover=true
    }

    fun difficultyAI(level: String)
    {
        when(level){
            "set_ai_easy_btn"->{
                    while (dealerCount<=12)
                        hit(DEALER)
                    if(dealerCount<=21 && userCount>21)
                        Log.d(tag, "Dealer won, user went over 21 ")
                    if(userCount<=21 && dealerCount>21)
                        Log.d(tag, "User won, dealer went over 21 ")
                    else if ((dealerCount> userCount)&& (dealerCount<=21))
                        Log.d(tag, "Dealer won with score of: "+ dealerCount)
                    else if ((dealerCount< userCount)&& (userCount<=21))
                        Log.d(tag, "User won with score of: "+ userCount)
                    else if((dealerCount>21 && userCount>21))
                        Log.d(tag, "It's a tie")
                    else if((dealerCount== userCount))
                        Log.d(tag, "It's a tie")
                Log.d(tag, "Easy")
            }
            "set_ai_normal_btn"->{

                while ((dealerCount<15 || userVisibleTotal()> dealerCount) && userVisibleTotal()<19)
                    hit(DEALER)
                if(dealerCount<=21 && userCount>21)
                    Log.d(tag, "Dealer won, user went over 21 ")
                if(userCount<=21 && dealerCount>21)
                    Log.d(tag, "User won, dealer went over 21 ")
                else if ((dealerCount> userCount)&& (dealerCount<=21))
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                else if ((dealerCount< userCount)&& (userCount<=21))
                    Log.d(tag, "User won with score of: "+ userCount)
                else if((dealerCount>21 && userCount>21))
                    Log.d(tag, "It's a tie")
                else if((dealerCount== userCount))
                    Log.d(tag, "It's a tie")
                Log.d(tag, "Normal")

            }
            "set_ai_hard_btn"->{
                //kinda cheating
                while (dealerCount<userCount && userCount<=21)
                    hit(DEALER)
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
            }
        }


    }

    fun confirm()
    {
        when(!gameover){
            true->{ //stand not pressed yet
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to start a new game?");
                builder.setTitle("New Game!")
                builder.setCancelable(false)

                builder.setPositiveButton("Yes") { dialog, which -> reset() }
                builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
                val alertDialog = builder.create()
                alertDialog.show();
            }
            false->{ // stand already pressed
                reset()
            }
        }


    }
    private fun isAce(string: String, card: Card)
    {

        if(string.equals(USER)&& card.num==1){
            userNumAces+=1
        }
        else if (string.equals(DEALER)&& card.num==1){
            dealerNumAces+=1
        }
    }
    private fun cardFormat(card: Card):String{
        return deck.getNum(card)+ " of "+ deck.getSuit(card)
        // 5 of Hearts or King of Diamonds format


    }
    private fun userVisibleTotal():Int
    {
        //users total minus their first card (the hidden one)
        return (userCount-deck.getIterator(USER).next().num)
    }

    companion object{
        const val tag="test"
        var userCount=0 // holds score of user
        var dealerCount=0 //holds dealers score
        var gameover = false
        var dealerNumAces=0
        var userNumAces=0
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