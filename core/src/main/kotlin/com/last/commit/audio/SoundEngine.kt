package com.last.commit.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.last.commit.TimeTravelAssetManager

object SoundEngine {

    var sfxVolume = 1.0f
    var musicVolume = 1.0f
        set(newValue) {
            field = newValue
            resume()
        }

    fun play(gameSound: String) {
        when (gameSound) {
            "STEPS_INDOOR" -> {
                val soundName = listOf(
                    "steps/steps_indoor_1.mp3",
                    "steps/steps_indoor_2.mp3",
                    "steps/steps_indoor_3.mp3",
                ).random()
                val sound = loadEffect(soundName)
                sound.play(sfxVolume)
            }

            "DOOR_OPEN" -> {
                val sound = loadEffect("door_open.mp3")
                sound.play(sfxVolume)
            }

            "DOOR_CLOSE" -> {
                val sound = loadEffect("door_close.mp3")
                sound.play(sfxVolume)
            }

            "TIME_TRAVEL" -> {
                val sound = loadEffect("time_travel.mp3")
                sound.play(sfxVolume)
            }

            "GRAB" -> {
                val sound = loadEffect("grab.mp3")
                sound.play(sfxVolume)
            }

            "BACKGROUND_MUSIC" -> {
                loadMusic("world_music.mp3").play()
            }

            else -> {
                println("Could not find sound/music $gameSound")
            }
        }
    }

    fun stop() {
        loadMusic("world_music.mp3").pause()
    }

    fun resume() {
        loadMusic("world_music.mp3").volume = musicVolume
        if (!loadMusic("world_music.mp3").isPlaying) {
            loadMusic("world_music.mp3").play()
        }
    }

    private fun loadEffect(name: String): Sound {
        return loadSound("effects/$name")
    }

    private fun loadMusic(name: String): Music {
        return TimeTravelAssetManager.get("sounds/music/$name")
    }

    private fun loadSound(name: String): Sound {
        return TimeTravelAssetManager.get("sounds/$name")
    }
}
