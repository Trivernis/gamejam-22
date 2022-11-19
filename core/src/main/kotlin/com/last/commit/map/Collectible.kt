package com.last.commit.map

import GameState
import Position
import com.badlogic.gdx.math.Rectangle
import com.last.commit.audio.GameSoundEffect
import com.last.commit.inventory.InventoryItem

class Collectible(
    name: String,
    val pos: Position,
    width: Float,
    height: Float,
    val requiredItem: String
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

    override fun canInteract(state: GameState): Boolean {
        if (requiredItem == "") {
            return true
        }
        val item: InventoryItem? = state.inventory.items.find { it.name == requiredItem }

        if (item == null) {
            return false
        }

        return true

    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}
