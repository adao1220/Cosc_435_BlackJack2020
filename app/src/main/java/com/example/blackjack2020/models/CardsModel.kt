package com.example.blackjack2020.models

import com.example.blackjack2020.CardRepository
import com.example.blackjack2020.Interfaces.ICardRepository
import kotlin.random.Random

class CardsModel(private val cardrepo: ICardRepository) {
    fun getValue(card:Card):Int{
        if(card.num<10)
            return card.num //will return 1-9
        else
            return 10 //will return 10,J,Q,K

        //doesnt account for ace being worth 11 yet
    }

    fun getRandomCard(): Card{

       return cardrepo.getRandomCard()
    }
    fun newGame(){
        cardrepo.newGame()
    }
    private fun getNum(card: Card):String{
        var num=card.num
        if(num in 2..10)
            return cardrepo.getValue(card).toString()
        else if(num==1)
            return "Ace"
        else if( num ==11)
            return "Jack"
        else if(num==12)
            return "Queen"
        else return "King"
    }
    private fun getSuit(card: Card):String
    {
        var suit=card.suit
        if(suit==1)
            return "clubs"
        else if (suit==2)
            return "diamonds"
        else if(suit==3)
            return "hearts"
        else
            return "spades"
    }
    fun cardFormat(card: Card):String{
        return getNum(card)+ " of "+ getSuit(card)
        // 5 of Hearts or King of Diamonds format


    }
    fun addToHand(card: Card, string: String)
    {
        if (string.equals("user")){
            cardrepo.addToHand(card,"user")
        }
        else{
            cardrepo.addToHand(card,"dealer")
        }

    }
    fun getHand(string: String)
    {
        if (string.equals("user")){
            cardrepo.getHand("user")
        }
        else{
            cardrepo.getHand("dealer")
        }



    }

    fun count():Int
    {
        return cardrepo.getCount()
    }



}