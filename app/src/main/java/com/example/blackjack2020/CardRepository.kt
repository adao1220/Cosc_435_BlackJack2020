package com.example.blackjack2020

import android.util.Log
import com.example.blackjack2020.Interfaces.ICardRepository
import com.example.blackjack2020.models.Card
import kotlin.random.Random.Default.nextInt

class CardRepository :ICardRepository{
    private var cards : MutableList<Card> = mutableListOf()
    private var userHand : MutableList<Card> = mutableListOf()
    private var dealerHand : MutableList<Card> = mutableListOf()

    init{
        cards.addAll((1..13).map{Card(1,"$it".toInt())}) //clubs
        cards.addAll((1..13).map{Card(2,"$it".toInt())}) //diamonds
        cards.addAll((1..13).map{Card(3,"$it".toInt())}) //hearts
        cards.addAll((1..13).map{ Card(4,"$it".toInt()) }) //spades

    }

    override fun getIterator(string: String): MutableIterator<Card> {
        when(string){
            USER->return userHand.iterator()
            else-> return dealerHand.iterator()
        }
    }

    override fun getValue(card:Card):Int{
        if(card.num<10)
            return card.num //will return 1-9
        else
            return 10 //will return 10,J,Q,K

        //doesnt account for ace being worth 11 yet
    }

    override fun getRandomCard(): Card{
        var chosenCard=Card(nextInt(1,4), nextInt(1,13))
        while(cards.indexOf(chosenCard)==-1) //this returns -1 when the card isn't in the deck, ie already been removed/used
            chosenCard=Card(nextInt(1,4), nextInt(1,13))
        cards.remove(chosenCard)
        return chosenCard

    }

    override fun getCount():Int{
        return cards.size
    }


    override fun getCards(): List<Card>
    {
        return cards
    }

    override fun removeCard(suit:Int, num:Int){
        cards.remove(Card(suit,num))
    }
    override fun removeCard(card: Card){
        cards.remove(card)
    }

    override fun newGame(){
        cards.removeAll(cards)
        cards.addAll((1..13).map{Card(1,"$it".toInt())}) //clubs
        cards.addAll((1..13).map{Card(2,"$it".toInt())}) //diamonds
        cards.addAll((1..13).map{Card(3,"$it".toInt())}) //hearts
        cards.addAll((1..13).map{Card(4,"$it".toInt())}) //spades

    }
    override fun addToHand(card: Card, string: String){
        if(string.equals("user")){
            userHand.add(card)
        }
        else{
            dealerHand.add(card)
        }
    }

    override fun getHand(string: String)
    {
        var cardsiterator = userHand.iterator()
        if(string.equals("user")) {
           cardsiterator = userHand.iterator()
        }
        else{
            cardsiterator = dealerHand.iterator()
        }
        var message= ""
        while(cardsiterator.hasNext())
        {
            message=cardFormat(cardsiterator.next())
            Log.d(TAG, message)
            //getNum(cardsiterator.next())
        }

    }

    private fun getNum(card: Card):String{
        var num=card.num
        if(num in 2..10)
            return card.num.toString()
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

    override fun stand(){

    }

    companion object{
        var TAG="test123"
    }

}