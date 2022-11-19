package com.last.commit.audio;

import com.last.commit.audio.GameSound

public class GameSoundEffect(name: String): GameSound(name) {
    companion object {
        val STEPS_INDOOR = GameSoundEffect("steps_indoor.mp3")
        val DOOR_OPEN = GameSoundEffect("door_open.mp3")
        val DOOR_CLOSE = GameSoundEffect("door_close.mp3")
    }
}