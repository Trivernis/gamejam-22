package com.last.commit.map

import com.badlogic.gdx.math.Rectangle

class Collectible(name: String, x: Float, y: Float, val gridX: Int, val gridY: Int, width: Float, height: Float) :
    Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(x, y, width, height)
    }

    override fun interact(otherCollider: Rectangle) {
        println("Interacting with item $name")
    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}