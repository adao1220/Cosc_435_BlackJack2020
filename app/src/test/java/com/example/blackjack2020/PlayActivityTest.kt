package com.example.blackjack2020

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class PlayActivityTest{
    val tested= PlayActivity().difficultyAI("easy")
    @Test
    fun `user wins returns 0`(){
        //sets two variables needed in difficultyAI
        PlayActivity.dealerCount=18
        PlayActivity.userCount=21
        assertThat(tested).isEqualTo(0)
    }
}
