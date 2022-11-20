package com.last.commit.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

public class SoundEngine(private val sfx: Float, private val music: Float) {


    var volumeSfx = sfx
    var volumeMusic = music
        set(newValue) {
            field = newValue
            resume()
        }

    private val sounds: ThreadLocal<HashMap<String, Sound>> =
        ThreadLocal.withInitial() { HashMap() }
    private val musicTracks: ThreadLocal<HashMap<String, Music>> =
        ThreadLocal.withInitial() { HashMap() }

    var backgroundMusic: Music

    init {
        backgroundMusic = loadMusic("world_music.mp3")
        resume()
    }

    fun play(gameSound: GameSound, ) {
        if (gameSound is GameSoundEffect) {
            val sound = loadEffect(gameSound.name)
            sound.play(volumeSfx)
        } else if (gameSound is GameMusic) {
            backgroundMusic.setLooping(true)
            resume()
        }
    }

    fun stop() {
        backgroundMusic.pause()
    }

    fun resume() {
        backgroundMusic.volume = volumeMusic
        if (!backgroundMusic.isPlaying) {
            backgroundMusic.play()
        }
    }
    private fun loadEffect(name: String): Sound {
        return loadSound("effects/$name")
    }

    private fun loadMusic(name: String): Music {
        var music = musicTracks.get().get(name)

        if (music == null) {
            println("Loading sound $name")
            music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music/$name"))
            musicTracks.get()[name] = music
        }
        return music!!
    }

    private fun loadSound(name: String): Sound {
        var sound = sounds.get().get(name)

        if (sound == null) {
            println("Loading sound $name")
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/$name"))
            sounds.get()[name] = sound
        }

        return sound!!
    }
}
