package com.last.commit.map

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle
import com.last.commit.Wall


class Door(gridX: Int, gridY: Int, wallCollider: Rectangle, cell: Cell) :
    Wall(gridX, gridY, wallCollider, cell), Interactable {
    override fun interact() {
        println("Toggling door $this")
        isOpen = !isOpen
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
            "Door: %f:%f - %f:%f (isOpen: %b)", wallCollider.x, wallCollider.y, wallCollider.width,
            wallCollider.height, isOpen
        )
    }
}