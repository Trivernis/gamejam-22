package com.last.commit.config

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class TimeTravelAssetManager : AssetManager() {

    val UI_TEXTURE_PATH = "ui/uiskin.json"

    init {
        loadTextures()
    }

    fun loadTextures() {
        load(UI_TEXTURE_PATH, Skin::class.java)
    }

    fun getUiTexture() : Skin {
        return get(UI_TEXTURE_PATH)
    }
}