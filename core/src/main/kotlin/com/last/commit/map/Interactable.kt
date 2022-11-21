package com.last.commit.map

import com.badlogic.gdx.math.Rectangle
import com.last.commit.GameState

interface Interactable {
    fun getCollider(): Rectangle

    fun interact(otherCollider: Rectangle, state: GameState): Boolean

    fun canInteract(state: GameState): Boolean
}