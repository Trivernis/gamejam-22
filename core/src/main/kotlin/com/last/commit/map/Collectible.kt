package com.last.commit.map

import com.badlogic.gdx.math.Rectangle

class Collectible(name: String, x: Float, y: Float, width: Float, height: Float) : Interactable {

    val name: String
    private val collider: Rectangle

    init {
        this.name = name
        this.collider = Rectangle(x, y, width, height)
    }

    override fun interact() {

    }

    override fun getCollider(): Rectangle {
        return this.collider
    }
}