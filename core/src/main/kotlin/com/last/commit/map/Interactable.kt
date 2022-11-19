package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import GameState

interface Interactable {
    fun getCollider(): Rectangle

    fun interact(otherCollider: Rectangle, state: GameState)
}