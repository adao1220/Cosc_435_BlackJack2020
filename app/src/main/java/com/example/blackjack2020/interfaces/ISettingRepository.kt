package com.example.blackjack2020.Interfaces

import com.example.blackjack2020.models.SettingModel

interface ISettingRepository {
    fun getSettings(): List<SettingModel>
    fun getSetting(idx:Int): SettingModel
    fun editSettings(idx: Int, setting: SettingModel)
    fun getCount(): Int
}