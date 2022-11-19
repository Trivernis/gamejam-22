package com.last.commit

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.math.Rectangle

open class Wall(var gridX: Int, var gridY: Int, wallCollider: Rectangle, cell: Cell) : Collidable {
    var wallCollider: Rectangle
    var cell: Cell

    init {
        this.wallCollider = wallCollider
        this.cell = cell
    }

    override fun getCollider(): Rectangle {
        return wallCollider
    }

    fun collidesWidth(collidable: Collidable): Boolean {
        return if (isColidable) {
            wallCollider.overlaps(collidable.getCollider())
        } else false
    }

    open val isColidable: Boolean
        get() = true

    override fun toString(): String {
        return java.lang.String.format(
            "Wall: %f:%f - %f:%f", wallCollider.x, wallCollider.y, wallCollider.width,
            wallCollider.height
        )
    }
}