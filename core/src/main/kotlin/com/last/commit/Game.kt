package com.last.commit

import com.badlogic.gdx.Game
import com.last.commit.inventory.Inventory
import GameState

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    private lateinit var state: GameState

    override fun create() {
        createState()
        setScreen(FirstScreen(state))
    }
    
    fun createState() {
        state = GameState(
            Inventory()
        )
    }
}