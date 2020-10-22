package com.example.blackjack2020.models

import com.example.blackjack2020.CardRepository
import com.example.blackjack2020.DEALER
import com.example.blackjack2020.Interfaces.ICardRepository
import com.example.blackjack2020.USER
import kotlin.random.Random

class CardsModel(private val cardrepo: ICardRepository) {
    fun getValue(card:Card):Int{
        return cardrepo.getValue(card)
    }

    fun getRandomCard(): Card{

       return cardrepo.getRandomCard()
    }
    fun newGame(){
        cardrepo.newGame()
    }


    fun getNum(card: Card):String{
        var num=card.num
        when(num){
            1->return "Ace"
            in 2..10-> return cardrepo.getValue(card).toString()
            11-> return "Jack"
            12-> return "Queen"
            else->return "King"

        }
    }


    fun getSuit(card: Card):String
    {
        var suit=card.suit
        when(suit){
            1->return "clubs"
            2->return "diamonds"
            3->return "hearts"
            else->return "spades"
        }

    }

    fun addToHand(card: Card, string: String)
    {
        when(string){
            USER->cardrepo.addToHand(card, USER)
            else->cardrepo.addToHand(card, DEALER)
        }
    }


    fun getIterator(string: String): MutableIterator<Card> {
        when(string){
            USER->return cardrepo.getIterator(USER)
            else->return cardrepo.getIterator(DEALER)
        }


    }

    fun count():Int
    {
        return cardrepo.getCount()
    }



}