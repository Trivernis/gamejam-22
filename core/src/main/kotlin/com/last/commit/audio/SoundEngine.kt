package com.last.commit.audio;

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import com.last.commit.audio.GameSound
import com.last.commit.audio.GameMusic


public class SoundEngine {

    private val sounds: HashMap<String, Sound> = HashMap()
    private var playingMusic: Music? = null
    
    public fun play(gameSound: GameSound, volume: Float = 1f) {
        if (gameSound is GameSoundEffect) {
            val sound = loadEffect(gameSound.name)
            sound.play(volume)
            println("Playing sound ${gameSound.name}")
        } else if (gameSound is GameMusic) {
            if (playingMusic != null && playingMusic!!.isPlaying()) {
                playingMusic!!.stop()
            }
            val music = loadMusic(gameSound.name)
            music.volume = volume
            music.setLooping(true)
            music.play()
            playingMusic = music
        }
    }
    
    private fun loadEffect(name: String): Sound { 
        return loadSound("effects/$name")
    }

    private fun loadMusic(name: String): Music {
        return Gdx.audio.newMusic(Gdx.files.internal("sounds/music/$name"))
    }

    private fun loadSound(name: String): Sound {
        var sound = sounds.get(name)

        if (sound == null) {
            println("Loading sound $name")
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/$name"))
            sounds[name] = sound
        }

        return sound!!
    }
}
