package com.example.blackjack2020

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.blackjack2020.models.Card
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*


private var deck= CardsModel(CardRepository())



class PlayActivity : AppCompatActivity() {
    private lateinit var cardImage : ImageView
    private var numPlayerCards : Int = 2
    private var numDealerCards : Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        var difficulty=""
        var backCard=""
        val options = intent.getStringExtra(MainActivity.LAUNCH_KEY)
        if (options!= null){
            val toSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            difficulty = toSet.difficulty
            backCard = toSet.card //string
        }
        Log.d(tag, backCard)

        this.cardImage = findViewById(R.id.dealer_card_1)
        //TODO change backcard to default card back onCreate
        //cardOneImage.setImageResource(drawableResource)

        play_hit_btn.setOnClickListener { hit("user")  }
        deal()
        play_new_game_btn.setOnClickListener{confirm()}
        play_stand_btn.setOnClickListener{stand(difficulty)}

        //this.cardOneImage = findViewById(R.id.card1)

    }


    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {
            //******USER********
            var card1 = deck.getRandomCard()
            deck.addToHand(card1,"user")
            this.cardImage = findViewById(R.id.player_card_1)
            changeImage(card1)
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            deck.addToHand(card2,"user")
            this.cardImage = findViewById(R.id.player_card_2)
            changeImage(card2)
            userCount += deck.getValue(card2)
            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            deck.addToHand(card1,"dealer")
            dealerCount += deck.getValue(card2)
            this.cardImage = findViewById(R.id.dealer_card_1)
            changeImage(card1)
            card2 = deck.getRandomCard()
            deck.addToHand(card2,"dealer")
            dealerCount += deck.getValue(card2)
//            this.cardImage = findViewById(R.id.dealer_card_2)
//            changeImage(card2)
            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)
            //*********


        }


    }

    fun changeImage(card: Card) {

        val drawableResource = when(card.suit){
            1 -> when(card.num){
                1 -> R.drawable.ac
                2 -> R.drawable.n2c
                3 -> R.drawable.n3c
                4 -> R.drawable.n4c
                5 -> R.drawable.n5c
                6 -> R.drawable.n6c
                7 -> R.drawable.n7c
                8 -> R.drawable.n8c
                9 -> R.drawable.n9c
                10 -> R.drawable.n10c
                11 -> R.drawable.jc
                12 -> R.drawable.qc
                13 -> R.drawable.kc
                else -> R.drawable.card_face_3 // TODO need to handle fail
            }
            2 -> when(card.num){
                1 -> R.drawable.ad
                2 -> R.drawable.n2d
                3 -> R.drawable.n3d
                4 -> R.drawable.n4d
                5 -> R.drawable.n5d
                6 -> R.drawable.n6d
                7 -> R.drawable.n7d
                8 -> R.drawable.n8d
                9 -> R.drawable.n9d
                10 -> R.drawable.n10d
                11 -> R.drawable.jd
                12 -> R.drawable.qd
                13 -> R.drawable.kd
                else -> R.drawable.card_face_3 // TODO need to handle fail
            }
            3 -> when(card.num){
                1 -> R.drawable.ah
                2 -> R.drawable.n2h
                3 -> R.drawable.n3h
                4 -> R.drawable.n4h
                5 -> R.drawable.n5h
                6 -> R.drawable.n6h
                7 -> R.drawable.n7h
                8 -> R.drawable.n8h
                9 -> R.drawable.n9h
                10 -> R.drawable.n10h
                11 -> R.drawable.jh
                12 -> R.drawable.qh
                13 -> R.drawable.kh
                else -> R.drawable.card_face_3 // TODO need to handle fail
            }
            3 -> when(card.num){
                1 -> R.drawable.`as`
                2 -> R.drawable.n2s
                3 -> R.drawable.n3s
                4 -> R.drawable.n4s
                5 -> R.drawable.n5s
                6 -> R.drawable.n6s
                7 -> R.drawable.n7s
                8 -> R.drawable.n8s
                9 -> R.drawable.n9s
                10 -> R.drawable.n10s
                11 -> R.drawable.js
                12 -> R.drawable.qs
                13 -> R.drawable.ks
                else -> R.drawable.card_face_3 // TODO need to handle fail
            }
            else -> R.drawable.card_face_3 // TODO need to handle fail
        }
        cardImage.setImageResource(drawableResource)
        return
    }



    fun hit(string: String){ //will allow a new card unless over score of 21
        if(string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                deck.addToHand(newCard, string)
                when(numPlayerCards){
                    2 -> {
                        this.cardImage = findViewById(R.id.player_card_3)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards ++
                    }
                    3 -> {
                        this.cardImage = findViewById(R.id.player_card_4)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards ++
                    }
                    4 -> {
                        this.cardImage = findViewById(R.id.player_card_5)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards ++
                    }
                    else -> {
                        Log.d(tag, "No more cards to flip")
                    }

                }
                changeImage(newCard)

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
        numPlayerCards = 2
        this.cardImage = findViewById(R.id.player_card_3)
        this.cardImage.visibility = View.INVISIBLE
        this.cardImage = findViewById(R.id.player_card_4)
        this.cardImage.visibility = View.INVISIBLE
        this.cardImage = findViewById(R.id.player_card_5)
        this.cardImage.visibility = View.INVISIBLE
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

    fun getCardImage(int: Int){

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