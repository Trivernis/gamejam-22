package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import com.last.commit.audio.GameSoundEffect
import Position
import GameState

class Collectible(
        name: String,
        val pos: Position,
        width: Float,
        height: Float
) : Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(pos.x, pos.y, width, height)
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
