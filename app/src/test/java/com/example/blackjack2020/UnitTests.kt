package com.example.blackjack2020

import android.provider.ContactsContract
import com.example.blackjack2020.SettingsActivity.Companion.TotalFunds
import com.example.blackjack2020.database.DatabaseHandler
import com.example.blackjack2020.models.Card
import com.example.blackjack2020.models.SettingModel
import com.google.common.truth.Truth.assertThat
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.junit.Test
import java.lang.Exception


class UnitTests{
    //val tested= PlayActivity().difficultyAI("set_ai_easy_btn")
    //var tested= PlayActivity()
    @Test
    fun `1) Deck has 52 cards in deck`(){
        var deck= CardRepository()
        assertThat(deck.getCount()).isEqualTo(52)
    }

    @Test
    fun `2) Suit 3, Num 12 returns Queen of Hearts`(){
        var deck= CardRepository()
        var card=Card(3,12)
        assertThat(deck.cardFormat(card)).isEqualTo("Queen of hearts")
    }

    @Test
    fun `3) Total cards decremented after dealing a card`(){
        var deck= CardRepository()
        deck.getRandomCard()
        assertThat(deck.getCount()).isEqualTo(51)
    }

    @Test
    fun `4) Dealercount less than usercount, user wins`(){
        var play=PlayActivity()
        play.setDealerCount(13)
        play.setUserCount(19)
        var difficultyEasy=play.difficultyAI("set_ai_easy_btn")
        assertThat(difficultyEasy).isEqualTo(0)
    }

    @Test
    fun `5) Usercount less than dealercount, dealer wins`(){
        var play=PlayActivity()
        play.setNumofDealerCards(2) // dealer has 2 cards
        play.setNumofPlayerCards(3) // user has 3 cards
        play.setDealerCount(17) // dealer score is 17
        play.setUserCount(13) //user score is 13
        var difficultyHard=play.difficultyAI("set_ai_hard_btn")
        assertThat(difficultyHard).isEqualTo(1)
    }

    @Test
    fun `6) User has 5 cards and under 21, user automatically wins`(){
        var play=PlayActivity()
        play.setNumofDealerCards(3) // dealer has 2 cards
        play.setNumofPlayerCards(5) // user has 3 cards
        play.setDealerCount(20) // dealer score is 17
        play.setUserCount(19) //user score is 13
        var difficultyHard=play.difficultyAI("set_ai_hard_btn")
        assertThat(difficultyHard).isEqualTo(0)
    }

    @Test
    fun `7) New game resets the deck`(){
        var deck= CardRepository()
        deck.getRandomCard()
        deck.newGame()
        assertThat(deck.getCount()).isEqualTo(52)
    }
    @Test
    fun `8) Ace Found, increments userNumofAces`(){
        var play=PlayActivity()
        var card= Card(1,1)
        play.isAce("user", card)
        assertThat(PlayActivity.userNumAces).isEqualTo(1)
    }
    @Test
    fun `9) Random Card gives different cards`(){
        var deck= CardRepository()
        var card= deck.getRandomCard()
        var card2= deck.getRandomCard()
        assertThat(deck.cardFormat(card)).isNotEqualTo(deck.cardFormat(card2))
    }

    @Test
    fun `10) 3 of diamonds is worth 3`(){
        var deck= CardRepository()
        var card= Card(2,3)
        assertThat(deck.getValue(card)).isEqualTo(3)
    }

    @Test
    fun `11) Addings funds to your current Pool of money`(){
        var Setting= SettingsActivity()
        val currFunds = 10.0
        val addFunds = 20.0
        val total = Setting.addToFunds(currFunds,addFunds)
        Setting.setTotalFunds(total)
        assertThat(TotalFunds).isEqualTo(30.0)
    }

    @Test
    fun `12) Betting system Losing side `(){
        var Game = PlayActivity()
        Game.setTotalFunds(40.0)
        Game.BetCalculation(10, "lost")
        assertThat(Game.newBalance).isEqualTo(30.0)
    }

    @Test
    fun `13) Checking the preset initial Values in Settings`(){
        var Setting = SettingRepository()
        val count = Setting.getCount()
        assertThat(count).isEqualTo(1)
    }

}
