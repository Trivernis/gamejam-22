package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.last.commit.audio.GameSoundEffect
import GameState

class Collectible(name: String, val pos: Position, private val size: Vector2) : Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(pos.x, pos.y, size.x, size.y)
    }

    override fun interact(otherCollider: Rectangle, state: GameState) {
        println("Interacting with item $name")
        state.soundEngine.play(GameSoundEffect.GRAB)
        state.inventory.add(this.name)
    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}
