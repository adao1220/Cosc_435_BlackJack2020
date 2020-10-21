package com.example.blackjack2020.models

data class SettingModel(
    val difficulty: String,
    val card: String,   //Todo: Might need to figure out how to pass imgs and set defaults
    val profileName: String,
    val funds: Double,
    val music: Boolean
)