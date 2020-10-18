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
        play_deal.setOnClickListener{deal()}
        play_hit_btn.setOnClickListener { hit("user")  }
        play_new_game_btn.setOnClickListener{reset()}
        play_stand_btn.setOnClickListener{stand()}




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
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2) + ". Card Count: " + deck.count()
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            deck.addToHand(card1,"dealer")
            dealerCount += deck.getValue(card2)

            card2 = deck.getRandomCard()
            deck.addToHand(card2,"dealer")
            dealerCount += deck.getValue(card2)

            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2) + ". Card Count: " + deck.count()
            Log.d(tag, message)
        }

    }

    fun hit(string: String){ //will allow a new card unless over score of 21
        if(string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                userCount += deck.getValue(newCard)

                var message = "Your new card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)
            } else
                Log.d(tag, "You've already lost")
        }
        else{
            if (dealerCount <= 21) {
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                dealerCount += deck.getValue(newCard)

                var message = "Dealers card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)
            }
        }

    }

    fun score(){
        var message= "Your Score is : "+ userCount
        Log.d(tag, message)
    }

    fun reset(){
        userCount=0
        dealerCount=0
        deck.newGame()
    }

    fun myhand(){
        deck.getHand("user")
    }
    fun stand(){
        Log.d(tag, "User Finished with score of: "+ userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: "+ dealerCount)
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

    }

    companion object{
        const val tag="test"
        var userCount=0 // holds score of user
        var dealerCount=0 //holds dealers score
    }

}