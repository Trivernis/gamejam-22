package com.last.commit.map

import com.badlogic.gdx.math.Rectangle

interface Interactable {
    fun getCollider(): Rectangle

    fun interact(otherCollider: Rectangle)
}