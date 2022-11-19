package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import Position

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

    override fun interact(otherCollider: Rectangle) {
        println("Interacting with item $name")
    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}
