package com.example.blackjack2020

import com.example.blackjack2020.models.Card
import com.google.common.truth.Truth.assertThat
import org.junit.Test


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
    fun `10) Ace Found, increments userNumofAces`(){
        var deck= CardRepository()
        var card= Card(2,3)
        assertThat(deck.getValue(card)).isEqualTo(3)
    }


}
