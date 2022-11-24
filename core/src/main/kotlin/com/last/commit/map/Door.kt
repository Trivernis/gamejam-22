package com.last.commit.map

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle
import com.last.commit.GameState
import com.last.commit.Wall
import com.last.commit.audio.SoundEngine
import com.last.commit.inventory.InventoryItem

class Door(gridX: Int, gridY: Int, wallCollider: Rectangle, cell: Cell) :
    Wall(gridX, gridY, wallCollider, cell), Interactable {


    override fun canInteract(state: GameState): Boolean {

        val requiredItem: String = cell.getTile().getProperties().get("requiredItem", "", String::class.java)

        val item: InventoryItem? = state.inventory.items.find { it.name == requiredItem }

        val result: Boolean
        if (item == null) {
            result = requiredItem == ""
        } else {
            result = requiredItem == item.name
        }

        if (!result) {
            state.dialogStage.setTexts("This dor is blocked. You need a key")
            state.dialogStage.show()
        }
        return result
    }

    override fun interact(otherCollider: Rectangle, state: GameState): Boolean {
        println("interacting with door $this")
        if (isClosed) {
            SoundEngine.play("DOOR_OPEN")
            isOpen = true
        } else if (isOpen) {
            if (getCollider().overlaps(otherCollider)) {
                // can't close the door cause it is colliding with given collider
            } else {
                SoundEngine.play("DOOR_CLOSE")
                isOpen = false
            }
        }

        println("Door is now open = $isOpen")
        return false
    }

    var isOpen: Boolean
        get() = !isClosed
        set(isOpen) {
            val currentRotation: Int = cell.getRotation()
            if (isOpen) {
                cell.setRotation(currentRotation + 1)
            } else {
                cell.setRotation(currentRotation - 1)
            }
        }
    val isClosed: Boolean
        get() {
            val currentRotation: Int = cell.getRotation()
            return currentRotation == 0
        }
    override val isCollidable: Boolean
        get() = !isOpen

    override fun toString(): String {
        return String.format(
            "Door: %f:%f - %f:%f (isOpen: %b)",
            wallCollider.x,
            wallCollider.y,
            wallCollider.width,
            wallCollider.height,
            isOpen
        )
    }
}
