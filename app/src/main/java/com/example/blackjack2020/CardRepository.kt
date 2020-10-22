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
    override fun getValue(card:Card):Int{

        when(card.num){
            1->{return 11}
            in 2..9->{return card.num}
            in 10..13->{return 10}
            else->{return 0}
        }

//        if(card.num<10)
//            return card.num //will return 1-9
//        else
//            return 10 //will return 10,J,Q,K

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

        when(string){
            USER->userHand.add(card)
            else->dealerHand.add(card)
        }
    }

    override fun getHand(string: String)
    {
        //prints out the users or dealers hand
        var cardsiterator: MutableIterator<Card>

        when(string){
            USER->cardsiterator = userHand.iterator()
            else->cardsiterator = dealerHand.iterator()
        }

        var message= ""
        while(cardsiterator.hasNext())
        {
            message=cardFormat(cardsiterator.next())
            Log.d(TAG, message)
            //getNum(cardsiterator.next())
        }

    }

    override fun getIterator(string: String): MutableIterator<Card> {
        when(string){
            USER->return userHand.iterator()
            else-> return dealerHand.iterator()
        }
    }

    private fun getNum(card: Card):String{
        var num=card.num
        when(num){
            1->return "Ace"
            in 2..10-> return card.num.toString()
            11-> return "Jack"
            12-> return "Queen"
            13-> return "King"
            else->return "" // this will never occur

        }
    }
    private fun getSuit(card: Card):String
    {
        var suit=card.suit
        when(suit){
            1->return "clubs"
            2->return "diamonds"
            3->return "hearts"
            4->return "spades"
            else->return ""// this will never occur
        }
    }
    fun cardFormat(card: Card):String{
        return getNum(card)+ " of "+ getSuit(card)
        // 5 of Hearts or King of Diamonds format


    }


    companion object{
        var TAG="test123"
    }

}