package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.audio.SoundEngine

class Collectible(
    name: String,
    val pos: Position,
    val size: Vector2,
    val requiredItem: String,
    val image: Image
) : Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(pos.x, pos.y, size.x, size.y)
    }

    override fun interact() {
        println("Interacting with item $name")
        SoundEngine.play("GRAB")
    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}
