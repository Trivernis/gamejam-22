package com.last.commit.audio;

import com.last.commit.audio.GameSound

public class GameSoundEffect(name: String): GameSound(name) {
    companion object {
        val STEPS_INDOOR: GameSoundEffect
            get() = listOf(
                GameSoundEffect("steps/steps_indoor_1.mp3"),
                GameSoundEffect("steps/steps_indoor_2.mp3"),
                GameSoundEffect("steps/steps_indoor_3.mp3"),
            ).random()
             
        val DOOR_OPEN = GameSoundEffect("door_open.mp3")
        val DOOR_CLOSE = GameSoundEffect("door_close.mp3")
        val TIME_TRAVEL = GameSoundEffect("time_travel.mp3")
        val GRAB = GameSoundEffect("grab.mp3")
    }
}