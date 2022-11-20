package com.last.commit.map

import GameState
import com.badlogic.gdx.math.Rectangle

interface Interactable {
    fun getCollider(): Rectangle

    fun interact(otherCollider: Rectangle, state: GameState)

    fun canInteract(state: GameState): Boolean
}