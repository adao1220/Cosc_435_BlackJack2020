package com.example.blackjack2020

import com.example.blackjack2020.models.Card

class CardRepository {
    private val cards : MutableList<Card> = mutableListOf()

    init{
        cards.addAll((1..13).map{Card(1,"$it".toInt())}) //clubs
        cards.addAll((1..13).map{Card(2,"$it".toInt())}) //diamonds
        cards.addAll((1..13).map{Card(3,"$it".toInt())}) //hearts
        cards.addAll((1..13).map{ Card(4,"$it".toInt()) }) //spades
    }

    fun getCount():Int{
        return cards.size
    }

    fun getCards(): List<Card>
    {
        return cards
    }

    fun removeCard(suit:Int, num:Int){
        cards.remove(Card(suit,num))
    }
    fun removeCard(card: Card){
        cards.remove(card)
    }

    fun newGame(){
        cards.removeAll(cards)
        cards.addAll((1..13).map{Card(1,"$it".toInt())}) //clubs
        cards.addAll((1..13).map{Card(2,"$it".toInt())}) //diamonds
        cards.addAll((1..13).map{Card(3,"$it".toInt())}) //hearts
        cards.addAll((1..13).map{Card(4,"$it".toInt())}) //spades

    }

}