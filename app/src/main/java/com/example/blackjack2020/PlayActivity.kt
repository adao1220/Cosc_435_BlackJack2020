package com.example.blackjack2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.blackjack2020.models.CardsModel
import kotlinx.android.synthetic.main.activity_play.*

private var deck= CardsModel(CardRepository())

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        //var deck= CardsModel(CardRepository())
        play_deal.setOnClickListener{deal()}
        play_hit_btn.setOnClickListener { hit() }
//        play_score_btn.setOnClickListener{score()}
        play_new_game_btn.setOnClickListener{reset()}
        //play_my_hand_btn.setOnClickListener{myhand()}




    }

    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {

            var card1 = deck.getRandomCard()
            count = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            count += deck.getValue(card2)
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2) + ". Card Count: " + deck.count()
            Log.d(tag, message)
            card1 = deck.getRandomCard()
            card2 = deck.getRandomCard()
            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2) + ". Card Count: " + deck.count()
            Log.d(tag, message)
        }

    }

    fun hit(){ //will allow a new card unless over score of 21
        if(count<=21) {
            var newCard = deck.getRandomCard()
            count += deck.getValue(newCard)

            var message = "Your new card is: " + deck.cardFormat(newCard)
            Log.d(tag, message)
        }
        else
            Log.d(tag, "You've already lost")

    }

    fun score(){
        var message= "Your Score is : "+ count
        Log.d(tag, message)
    }

    fun reset(){
        count=0
        deck.newGame()
    }

//    fun myhand(){
//        deck.getHand()
//    }

    companion object{
        const val tag="test"
        var count=0 // holds score of user
    }

}