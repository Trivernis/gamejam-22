package com.last.commit.map

import GameState
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle
import com.last.commit.Wall
import com.last.commit.audio.GameSoundEffect

class Door(gridX: Int, gridY: Int, wallCollider: Rectangle, cell: Cell) :
        Wall(gridX, gridY, wallCollider, cell), Interactable {

    override fun interact(otherCollider: Rectangle, state: GameState) {
        println("interacting with door $this")
        if (isClosed) {
            state.soundEngine.play(GameSoundEffect.DOOR_OPEN)
            isOpen = true
        } else if (isOpen) {
            if (getCollider().overlaps(otherCollider)) {
                // can't close the door cause it is colliding with given collider
            } else {
                state.soundEngine.play(GameSoundEffect.DOOR_CLOSE)
                isOpen = false
            }
        }

        println("Door is now open = $isOpen")
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
