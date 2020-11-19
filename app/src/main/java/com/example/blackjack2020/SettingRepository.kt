package com.example.blackjack2020

import com.example.blackjack2020.Interfaces.ISettingRepository

import com.example.blackjack2020.models.SettingModel

class SettingRepository: ISettingRepository {
    val set: MutableList<SettingModel> = mutableListOf()
    init {
        set.add(
            SettingModel(1, "set_ai_easy_btn", "cardface1", "Alex", totalFunds, "True")
        )
    }

    override fun getSettings(): List<SettingModel> {
        return set
    }

    override fun getSetting(idx: Int): SettingModel {
        if (idx < 0) return set[0]
        if (idx >= getCount()) return set[set.size - 1]
        return set[idx]
    }

    override fun editSettings(idx: Int, setting: SettingModel) {
        set[idx]=setting
    }

    override fun getCount(): Int {
        return set.size
    }


}