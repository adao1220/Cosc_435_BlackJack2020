package com.example.blackjack2020

import android.app.AlertDialog
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.blackjack2020.models.Card
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.blackjack2020.SettingsActivity.Companion.TotalFunds
import com.example.blackjack2020.SettingsActivity.Companion.card
import com.example.blackjack2020.SettingsActivity.Companion.difficulty
import com.example.blackjack2020.Play_Settings_fragment.Companion.name
import com.example.blackjack2020.SettingsActivity.Companion.TAG
import com.example.blackjack2020.models.CardsModel
import com.example.blackjack2020.models.SettingModel

import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_play.*


private var deck= CardsModel(CardRepository())
const val USER="user"
const val DEALER="dealer"

class PlayActivity : AppCompatActivity() {
    private lateinit var cardImage: ImageView
    private var numPlayerCards: Int = 2
    private var numDealerCards: Int = 2

    private lateinit var hiddenCard: Card
    private var BetView: TextView? = null
    private var BetBarView: SeekBar? = null


    var newBalance = 0.0


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)


        val options = intent.getStringExtra(MainActivity.LAUNCH_KEY)
        if (options != null) {
            val FromSet = Gson().fromJson<SettingModel>(options, SettingModel::class.java)
            difficulty = FromSet.difficulty
            name = FromSet.profileName
            card = FromSet.card
            TotalFunds = FromSet.funds
            newBalance = TotalFunds
            play_cash.text = "Total Cash: $" + TotalFunds.toString()
            max = TotalFunds.toInt()
        }

        this.cardImage = findViewById(R.id.dealer_card_1)
        BetView = this.play_current_bet
        BetBarView = this.play_betbar
        BetBarView!!.max = (max - min) / step
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

        play_changeSetting.setOnClickListener { launchfragment() }

        play_cash.text = "Total Cash: $" + TotalFunds.toString()

        play_hit_btn.setOnClickListener { hit("user", currentBet) }
        play_new_game_btn.setOnClickListener { confirm() }
        play_stand_btn.setOnClickListener { stand(difficulty, currentBet) }
    }

    fun launchfragment() {
        val frag = Play_Settings_fragment()
        val args = Bundle()
        args.putString(Play_Settings_fragment.cardface, card)
        args.putString(Play_Settings_fragment.difficulty, difficulty)
        args.putString(Play_Settings_fragment.funds, TotalFunds.toString())
        args.putString(Play_Settings_fragment.name, name)
        frag.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(R.id.flfragment, frag, null)
            //.addToBackStack(null)
            .commit()
    }


    fun deal() { //can only "deal" from a full deck, if less than full you need a new game
        if (deck.count() == 52) {
            var card1 = deck.getRandomCard()
            //var card1=(Card(1,1))
            deck.addToHand(card1, "user")
            isAce(USER, card1)
            this.cardImage = findViewById(R.id.player_card_1)
            changeImage(card1)
            userCount = deck.getValue(card1)
            var card2 = deck.getRandomCard()
            //var card2=(Card(2,1))
            deck.addToHand(card2, "user")
            isAce(USER, card2)
            this.cardImage = findViewById(R.id.player_card_2)
            changeImage(card2)
            userCount += deck.getValue(card2)
            if (userCount > 21 && userNumAces > 0) //checks for aces
            {
                userCount -= 10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                Log.d(tag, "TREATING ACE AS 1")
                userNumAces--
            }
//            play_score.text= userCount.toString()
            var message =
                "Your cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)

            //******DEALER***********
            card1 = deck.getRandomCard()
            //card1=(Card(3,1))
            isAce(DEALER, card1)
            deck.addToHand(card1, "dealer")
            dealerCount += deck.getValue(card1)
            this.cardImage = findViewById(R.id.dealer_card_1)
            changeImage(card1)
            card2 = deck.getRandomCard()
            //card2=(Card(4,1))
            hiddenCard = card2
            isAce(DEALER, card2)
            deck.addToHand(card2, "dealer")
            dealerCount += deck.getValue(card2)
            if (dealerCount > 21 && dealerNumAces > 0) //checks for aces
            {
                dealerCount -= 10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                Log.d(tag, "TREATING ACE AS 1")
                dealerNumAces--
            }
            this.cardImage = findViewById(R.id.dealer_card_2)
            changeImage(card2)
            when (card) {
                "cardface1" -> {
                    Log.d(tag, " 1. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_1)
                }
                "cardface2" -> {
                    Log.d(tag, " 2. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_2)
                }
                "cardface3" -> {
                    Log.d(tag, " 3. Dealer second selected!")
                    cardImage.setImageResource(R.drawable.card_face_3)
                }
            }


            message =
                "Dealers cards are: " + deck.cardFormat(card1) + " and " + deck.cardFormat(card2)
            Log.d(tag, message)
            //*********


        }


    }

    fun changeImage(card: Card) {

        val drawableResource = when (card.suit) {
            1 -> when (card.num) {
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
            2 -> when (card.num) {
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
            3 -> when (card.num) {
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
            4 -> when (card.num) {
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


    fun hit(string: String, currentBet: Int) { //will allow a new card unless over score of 21
        this.play_betbar.visibility = View.INVISIBLE
        if (string.equals("user")) {
            if (userCount <= 21) {
                var newCard = deck.getRandomCard()
                isAce(USER, newCard)
                deck.addToHand(newCard, string)
                //numPlayerCards++
                when (numPlayerCards) {
                    2 -> {
                        this.cardImage = findViewById(R.id.player_card_3)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards++
                    }
                    3 -> {
                        this.cardImage = findViewById(R.id.player_card_4)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards++
                    }
                    4 -> {
                        this.cardImage = findViewById(R.id.player_card_5)
                        this.cardImage.visibility = View.VISIBLE
                        numPlayerCards++
                    }
                    else -> {
                        Log.d(tag, "No more cards to flip")
                    }

                }
                changeImage(newCard)

                userCount += deck.getValue(newCard)
                if (userCount > 21 && userNumAces > 0) //checks for aces
                {
                    userCount -= 10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                    Log.d(tag, "TREATING ACE AS 1")
                    userNumAces--
                }
//                play_score.text= userCount.toString()

                var message = "Your new card is: " + deck.cardFormat(newCard)
                Log.d(tag, message)
            } else {
                Log.d(tag, "You've already lost")
                play_hit_btn.isClickable = false
                play_stand_btn.isClickable = false
                lostBet(currentBet)

            }
            if (userCount > 21) {
                play_hit_btn.isClickable = false
            }
        } else {
            var newCard = deck.getRandomCard()
            deck.addToHand(newCard, string)
            dealerCount += deck.getValue(newCard)
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
            var message = "Dealers card is: " + deck.cardFormat(newCard)
            if (dealerCount > 21 && dealerNumAces > 0) //checks for aces
            {
                dealerCount -= 10 // if the user has an ace, and over 21 will count the ace as 1 rather than 11
                Log.d(tag, "TREATING ACE AS 1")
                dealerNumAces--
            }
            Log.d(tag, message)

        }

    }


    fun stand(difficultyString: String, currentBet: Int) {
        Log.d(tag, "User Finished with score of: " + userCount + "\n Dealers Turn")
        Log.d(tag, "Dealer count: " + dealerCount)
        this.cardImage = findViewById(R.id.dealer_card_2)
        changeImage(hiddenCard)
        play_hit_btn.isClickable = false
        play_stand_btn.isClickable = false
        val winLose = difficultyAI(difficultyString)
        when (winLose) {
            0 -> wonBet(currentBet)
            1 -> lostBet(currentBet)
        }
        gameover = true

    }

    fun lostBet(currentBet: Int) {
        BetCalculation(currentBet, "lost")

        play_cash.text = "Total Cash: $" + TotalFunds.toString()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("You lost :(  Do you want to play again? ");
        builder.setTitle("Loser!")
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { dialog, which -> reset() }
        builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show();

    }



    fun wonBet(currentBet: Int) {
        BetCalculation(currentBet, "won")

        play_cash.text = "Total Cash: $" + TotalFunds.toString()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("You won:)  Do you want to play again? ");
        builder.setTitle("Winner!")
        builder.setCancelable(false)

        builder.setPositiveButton("Yes") { dialog, which -> reset() }
        builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alertDialog = builder.create()
        alertDialog.show();
    }

    fun BetCalculation(CurrentBet: Int,result: String): Double{
        var newFun = TotalFunds
        if(result.equals("lost")){
            TotalFunds = newFun - CurrentBet
        }
        else if(result.equals("won")){
            TotalFunds = newFun + CurrentBet
        }
        newBalance = TotalFunds
        return TotalFunds
    }

    fun difficultyAI(level: String): Int {
        when (level) {
            "set_ai_easy_btn" -> {
                while (dealerCount <= 12)
                    hit(DEALER, currentBet)
                if (dealerCount <= 21 && userCount > 21) {
                    //Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if (userCount <= 21 && dealerCount > 21) {
                    //Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                if(numDealerCards>4)
                    return 1
                if(numPlayerCards>4)
                    return 0
                else if ((dealerCount > userCount) && (dealerCount <= 21)) {
                    //Log.d(tag, "Dealer won with score of: " + dealerCount)
                    return 1
                } else if ((dealerCount < userCount) && (userCount <= 21)) {
                    //Log.d(tag, "User won with score of: " + userCount)
                    return 0
                } else if ((dealerCount > 21 && userCount > 21)) {
                    //Log.d(tag, "It's a tie")
                    return 1
                } else if ((dealerCount == userCount)) {
                    //Log.d(tag, "It's a tie")
                    return 1
                }
                //Log.d(tag, "Easy")
            }
            "set_ai_normal_btn" -> {

                while ((dealerCount < 15 || userVisibleTotal() > dealerCount) && userVisibleTotal() < 19)
                    hit(DEALER, currentBet)
                if (dealerCount <= 21 && userCount > 21) {
                    //Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if (userCount <= 21 && dealerCount > 21) {
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                if(numDealerCards>4)
                    return 1
                if(numPlayerCards>4)
                    return 0
                else if ((dealerCount > userCount) && (dealerCount <= 21)) {
                    Log.d(tag, "Dealer won with score of: " + dealerCount)
                    return 1
                } else if ((dealerCount < userCount) && (userCount <= 21)) {
                    Log.d(tag, "User won with score of: " + userCount)
                    return 0
                } else if ((dealerCount > 21 && userCount > 21)) {
                    Log.d(tag, "It's a tie")
                    return 1
                } else if ((dealerCount == userCount)) {
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Normal")

            }
            "set_ai_hard_btn" -> {
                //kinda cheating
                while (dealerCount < userCount && userCount <= 21)
                    hit(DEALER, currentBet)
                if (dealerCount <= 21 && userCount > 21) {
                    Log.d(tag, "Dealer won, user went over 21 ")
                    return 1
                }
                if (userCount <= 21 && dealerCount > 21) {
                    Log.d(tag, "User won, dealer went over 21 ")
                    return 0
                }
                if(numDealerCards>4)
                    return 1
                if(numPlayerCards>4)
                    return 0
                else if ((dealerCount > userCount) && (dealerCount <= 21)) {
                    //Log.d(tag, "Dealer won with score of: " + dealerCount)
                    return 1
                } else if ((dealerCount < userCount) && (userCount <= 21)) {
                    Log.d(tag, "User won with score of: " + userCount)
                    return 0
                } else if ((dealerCount == userCount)) {
                    Log.d(tag, "It's a tie")
                    return 1
                }
                Log.d(tag, "Hard")
            }
        }
        return 3

    }

    private fun userVisibleTotal(): Int {
        //users total minus their first card (the hidden one)
        return (userCount - deck.getIterator(USER).next().num)
    }

    fun reset() {

        numPlayerCards = 2 //reset the players hand
        numDealerCards = 2//
        userNumAces = 0
        dealerNumAces = 0


        this.play_betbar.visibility = View.VISIBLE
        dealer_card_3.visibility = View.INVISIBLE
        dealer_card_4.visibility = View.INVISIBLE
        dealer_card_5.visibility = View.INVISIBLE
        player_card_3.visibility = View.INVISIBLE
        player_card_4.visibility = View.INVISIBLE
        player_card_5.visibility = View.INVISIBLE



        if (TotalFunds < 5) {

            play_hit_btn.isClickable = false
            play_stand_btn.isClickable = false
            play_new_game_btn.isClickable = false
            Toast.makeText(this@PlayActivity, "You are poor, add more funds", Toast.LENGTH_SHORT)
                .show()
        } else {
            userCount = 0
            play_hit_btn.isClickable = true
            play_stand_btn.isClickable = true
            play_new_game_btn.isClickable = true
            dealerCount = 0
            gameover = false
            deck.newGame()
            max = TotalFunds.toInt()
            BetBarView!!.max = (max - min) / step
            deal()


        }}


        fun confirm() {
            when (!gameover) {
                true -> {
                    val builder = AlertDialog.Builder(this)
                    Log.i(TAG,"UserCount: "+ numPlayerCards)
                    if(numPlayerCards>2){builder.setMessage("You will lose $"+ currentBet)}
                    else{builder.setMessage("You will lose $5")}

                    builder.setTitle("New Game!")
                    builder.setCancelable(false)

                    builder.setPositiveButton("Yes") { dialog, which -> prematureloss() }
                    builder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
                    val alertDialog = builder.create()
                    alertDialog.show();
                }
                false -> {
                    reset()
                }
            }
        }

    private fun prematureloss(){
        var newFun = TotalFunds
        if(numPlayerCards>2){
            TotalFunds = newFun - currentBet
        }
        else
            TotalFunds=newFun-5
        play_cash.text = "Total Cash: $" + TotalFunds.toString()
        reset()
    }

         fun isAce(string: String, card: Card) {
            if (string.equals(USER) && card.num == 1) {
                //Log.i(TAG,"ACE FOUND : USER")
                userNumAces += 1
            } else if (string.equals(DEALER) && card.num == 1) {
                dealerNumAces += 1
                Log.i(TAG,"ACE FOUND : DEALER")
            }
        }

        companion object{
            var tag = "test"

            var userCount = 0 // holds score of user
            var dealerCount = 0 //holds dealers score
            var gameover = false
            var dealerNumAces = 0
            var userNumAces = 0
            var max = 0
            var min = 5
            var step = 1
            var currentBet = 5

        }


        fun updateCash() {
            play_cash.text = "Total Cash: $" + TotalFunds.toString()

        }


    fun setUserCount(user:Int){
        userCount=user
    }
    fun setDealerCount(dealer: Int)
    {
        dealerCount=dealer
    }
    fun setDifficulty(Difficulty: String)
    {
        difficulty=Difficulty
    }
    fun setGameover(flag: Boolean)
    {
        gameover=flag
    }
    fun setDealerNumAces(num: Int)
    {
        dealerNumAces=num
    }
    fun setUserNumAces(num: Int)
    {
        userNumAces=num
    }
    fun setTotalFunds(money: Double)
    {
        newBalance=money
        TotalFunds = money
    }
    fun setCurrentBet(money: Int)
    {
        currentBet=money
    }
    fun setTag()
    {
        tag="test"
    }
    fun setDeck(){
        deck= CardsModel(CardRepository())
    }
    fun setNumofPlayerCards(num: Int){
        numPlayerCards= num
    }
    fun getNumofPlayerCards():Int{
        return numPlayerCards
    }
    fun setNumofDealerCards(num: Int){
        numDealerCards= num
    }




}