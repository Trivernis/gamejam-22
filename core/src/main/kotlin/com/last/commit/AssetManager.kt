package com.last.commit

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object TimeTravelAssetManager {

    private val skin = "ui/uiskin.json"

    private val assetManager = AssetManager()
    fun loadSounds(vararg paths: String) {
        println("Loading ${paths.size} sounds")
        for (path in paths) {
            assetManager.load(path, Sound::class.java)
        }
    }

    fun loadMusics(vararg paths: String) {
        println("Loading ${paths.size} musics")
        for (path in paths) {
            assetManager.load(path, Music::class.java)
        }
    }

    fun loadSkins(vararg paths: String) {
        println("Loading ${paths.size} skins")
        for (path in paths) {
            assetManager.load(path, Skin::class.java)
        }
        assetManager.load(skin, Skin::class.java);
    }

    fun loadTextures(vararg paths: String) {
        println("Loading ${paths.size} textures")
        for (path in paths) {
            assetManager.load(path, Music::class.java)
        }
    }

    fun getSkin(): Skin {
        return assetManager.get(skin)
    }

    fun <T> get(path: String): T {
        return assetManager.get(path)
    }

    fun finishLoading() {
        assetManager.finishLoading()
    }


    fun update(): Boolean {
        return assetManager.update()
    }

    fun dispose() {
        assetManager.dispose()
    }
}