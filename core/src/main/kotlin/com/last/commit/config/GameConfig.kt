package com.last.commit.config

import kotlin.math.floor

class GameConfig {
    private var maps: ArrayList<String> = ArrayList()

    fun getRandomMap(): String {
        val randomMapIndex = floor(Math.random() * maps.size)
        System.out.println(randomMapIndex)
        return maps[randomMapIndex.toInt()]
    }
}