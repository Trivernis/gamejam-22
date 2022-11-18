package com.last.commit.inventory

import com.badlogic.gdx.graphics.g2d.TextureRegion

class InventoryItem(name: String, texture: TextureRegion) {
    val texture: TextureRegion
    val name: String

    init {
        this.name = name
        this.texture = texture
    }
}