package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.last.commit.GameState
import com.last.commit.audio.GameSoundEffect
import com.last.commit.inventory.InventoryItem

class Collectible(
    name: String,
    val pos: Position,
    val size: Vector2,
    val requiredItem: String
) : Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(pos.x, pos.y, size.x, size.y)
    }

    override fun interact(otherCollider: Rectangle, state: GameState) {
        println("Interacting with item $name")
        state.soundEngine.play(GameSoundEffect.GRAB)
        if (state.inventory.add(this.name, state)) {
            state.map?.collectibles?.remove(this)
        }
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
