package com.last.commit.audio;

import com.last.commit.audio.GameSound

public class GameSoundEffect(name: String): GameSound(name) {
    companion object {
        val STEPS_INDOOR = GameSoundEffect("steps_indoor.mp3");
    }
}