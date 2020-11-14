package com.example.blackjack2020

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.blackjack2020.models.Card
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*


private var deck= CardsModel(CardRepository())
const val USER="user"
const val DEALER="dealer"

class PlayActivity : AppCompatActivity() {
    private lateinit var cardImage : ImageView
    private lateinit var backcardImage : ImageView
    private var numPlayerCards : Int = 2
    private var numDealerCards : Int = 2

    private lateinit var hiddenCard: Card
    private var BetView: TextView? = null
    private var BetBarView: SeekBar? = null

    private var min = 5
    private var max = 0
    private var step = 5
    private var currentBet = 5
    private var newBalance = 0.0
    private var backCard=""
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        var difficulty=""



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
        //Log.d(tag, backCard)






        //this.backcardImage = findViewById(R.id.dealer_card_2)

        this.cardImage = findViewById(R.id.dealer_card_1)
        //TODO change backcard to default card back onCreate
        //cardOneImage.setImageResource(drawableResource)


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
        reset()


        //Todo refactor total funds
        play_hit_btn.setOnClickListener { hit("user", currentBet)  }
        play_new_game_btn.setOnClickListener{confirm()}
        play_stand_btn.setOnClickListener{stand(difficulty, currentBet)}
    }


    fun deal(){ //can only "deal" from a full deck, if less than full you need a new game
        if(deck.count()==52) {
            var card1 = deck.getRandomCard()
            deck.addToHand(card1,"user")
            isAce(USER, card1)
            this.cardImage = findViewById(R.id.player_card_1)
            changeImage(card1)
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            deck.addToHand(card2,"user")
            isAce(USER, card2)
            this.cardImage = findViewById(R.id.player_card_2)
            changeImage(card2)
            userCount += deck.getValue(card2)
            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            isAce(DEALER, card1)
            deck.addToHand(card1,"dealer")
            dealerCount += deck.getValue(card1)
            this.cardImage = findViewById(R.id.dealer_card_1)
            changeImage(card1)
            card2 = deck.getRandomCard()
            hiddenCard = card2
            isAce(DEALER, card2)
            deck.addToHand(card2,"dealer")
            dealerCount += deck.getValue(card2)
            this.cardImage = findViewById(R.id.dealer_card_2)
            changeImage(card2)
            when(backCard){
                "cardface1"->{
                    Log.d(tag, " 1. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_1)
                }
                "cardface2"->{
                    Log.d(tag, " 2. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_2)                }
                "cardface3"->{
                    Log.d(tag, " 3. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_3)                }
            }


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
                else -> R.drawable.card_face_1 // TODO need to handle fail
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
                else -> R.drawable.card_face_1 // TODO need to handle fail
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
                else -> R.drawable.card_face_1 // TODO need to handle fail
            }
            4 -> when(card.num){
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
                else -> R.drawable.card_face_1 // TODO need to handle fail
            }
            else -> R.drawable.card_face_1 // TODO need to handle fail
        }
        cardImage.setImageResource(drawableResource)
        return
    }


    fun hit(string: String, currentBet: Int){ //will allow a new card unless over score of 21
        if(string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                isAce(USER, newCard)
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
                if(userCount>21 && userNumAces>0) //checks for aces
                {
                    userCount-=10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                    Log.d(tag, "TREATING ACE AS 1")
                    userNumAces--
                }
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
            isAce(DEALER, newCard)
            deck.addToHand(newCard, string)
            when (numDealerCards) {
                2 -> {
                    this.cardImage = findViewById(R.id.dealer_card_3)
                    this.cardImage.visibility = View.VISIBLE
                    numDealerCards++
                }
                3 -> {
                    this.cardImage = findViewById(R.id.dealer_card_4)
                    this.cardImage.visibility = View.VISIBLE
                    numDealerCards++
                }
                4 -> {
                    this.cardImage = findViewById(R.id.dealer_card_5)
                    this.cardImage.visibility = View.VISIBLE
                    numDealerCards++
                }
                else -> {
                    Log.d(tag, "No more cards to flip")
                }

            }
            changeImage(newCard)

            dealerCount += deck.getValue(newCard)

            var message = "Dealers card is: " + deck.cardFormat(newCard)
            Log.d(tag, message)
        }






    fun stand(difficultyString: String, currentBet: Int){
        Log.d(tag, "User Finished with score of: " + userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: " + dealerCount)
        this.cardImage = findViewById(R.id.dealer_card_2)
        changeImage(hiddenCard)
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




    fun difficultyAI(level: String): Int
    {
        when(level){
            "set_ai_easy_btn"->{
                while (dealerCount<=12)
                    dealerHit("Dealer")
                if(dealerCount<=21 && userCount>21){
                    Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if(userCount<=21 && dealerCount>21){
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                else if ((dealerCount> userCount)&& (dealerCount<=21)){
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                    return 1
                }
                else if ((dealerCount< userCount)&& (userCount<=21)) {
                    Log.d(tag, "User won with score of: "+ userCount)
                    return 0
                }
                else if((dealerCount>21 && userCount>21)){
                    Log.d(tag, "It's a tie")
                    return 1
                }
                else if((dealerCount== userCount)){
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Easy")
            }
            "set_ai_normal_btn"->{

                while ((dealerCount<15 || userVisibleTotal()> dealerCount) && userVisibleTotal()<19)
                    dealerHit("Dealer")
                if(dealerCount<=21 && userCount>21){
                    Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if(userCount<=21 && dealerCount>21){
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                else if ((dealerCount> userCount)&& (dealerCount<=21)){
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                    return 1
                }
                else if ((dealerCount< userCount)&& (userCount<=21)){
                    Log.d(tag, "User won with score of: "+ userCount)
                    return 0
                }
                else if((dealerCount>21 && userCount>21)){
                    Log.d(tag, "It's a tie")
                    return 1
                }
                else if((dealerCount== userCount)){
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Normal")

            }
            "set_ai_hard_btn"->{
                //kinda cheating
                while (dealerCount<userCount && userCount<=21)
                    dealerHit("Dealer")
                if(dealerCount<=21 && userCount>21){
                    Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if(userCount<=21 && dealerCount>21){
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                else if ((dealerCount> userCount)&& (dealerCount<=21)){
                    Log.d(tag, "Dealer won with score of: "+ dealerCount)
                    return 1
                }
                else if ((dealerCount< userCount)&& (userCount<=21)){
                    Log.d(tag, "User won with score of: "+ userCount)
                    return 0
                }
                else if((dealerCount== userCount)){
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Hard")
            }
        }
        return 3

    }
    private fun userVisibleTotal():Int {
        //users total minus their first card (the hidden one)
        return (userCount-deck.getIterator(USER).next().num)
    }
    fun reset(){

        numPlayerCards = 2 //reset the players hand
        numDealerCards = 2//
        userNumAces=0
        dealerNumAces=0

        dealer_card_3.visibility = View.INVISIBLE
        dealer_card_4.visibility = View.INVISIBLE
        dealer_card_5.visibility = View.INVISIBLE
        player_card_3.visibility = View.INVISIBLE
        player_card_4.visibility = View.INVISIBLE
        player_card_5.visibility = View.INVISIBLE


        if(totalFunds <= 0.0){
            play_hit_btn.isClickable = false
            play_stand_btn.isClickable = false
            play_new_game_btn.isClickable = false
            Toast.makeText(this@PlayActivity, "You are poor, add more funds", Toast.LENGTH_SHORT).show()
        }
        else{
            userCount=0
            play_hit_btn.isClickable = true
            play_stand_btn.isClickable = true
            play_new_game_btn.isClickable = true
            dealerCount=0
            gameover=false
            deck.newGame()
            max = totalFunds.toInt()
            BetBarView!!.max = (max -min)/ step
            deal()
        }
    }


    fun confirm() {
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

    private fun isAce(string: String, card: Card) {
        if(string.equals(USER)&& card.num==1){
            userNumAces+=1
        }
        else if (string.equals(DEALER)&& card.num==1){
            dealerNumAces+=1
        }
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