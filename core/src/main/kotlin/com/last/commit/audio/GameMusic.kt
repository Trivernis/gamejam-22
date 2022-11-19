package com.last.commit.audio

import com.last.commit.audio.GameSound

class GameMusic(name: String): GameSound(name) {
    companion object {
        val WORLD_MUSIC = GameMusic("world_music.mp3")
    }
}