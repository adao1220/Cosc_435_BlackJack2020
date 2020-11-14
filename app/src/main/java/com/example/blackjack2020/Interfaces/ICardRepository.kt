package com.example.blackjack2020.Interfaces

import com.example.blackjack2020.models.Card
import kotlin.random.Random

interface ICardRepository {

    fun getValue(card: Card):Int
    fun getRandomCard(): Card
    fun getCount():Int
    fun getCards(): List<Card>
    fun removeCard(suit:Int, num:Int)
    fun removeCard(card: Card)
    fun newGame()
    fun getHand(string: String)
    fun addToHand(card: Card, string: String)
    fun stand()
    fun getIterator(string: String): MutableIterator<Card>


}