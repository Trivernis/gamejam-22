package com.last.commit

import GameState
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.last.commit.audio.SoundEngine
import com.last.commit.config.GameSettings
import com.last.commit.inventory.Inventory
import com.last.commit.screen.*
import java.awt.AWTEventMulticaster


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    lateinit var state: GameState

    lateinit var menu : TimeTravelScreen
    lateinit var gameplay: TimeTravelScreen
    lateinit var settings: TimeTravelScreen
    lateinit var inputProcessor: GameInputProcessor

    override fun create() {
        createState()
        createScreens()
        changeScreen(Screens.GAME)

        inputProcessor = GameInputProcessor(this)
        Gdx.input.inputProcessor = inputProcessor
    }

    private fun createScreens() {

        menu = MainMenu(this)
        gameplay = FirstScreen(this)
        settings = Settings(this)
    }

    fun changeScreen(screen : Screens) {
        when (screen) {
            Screens.MAIN_MENU -> setScreen(menu)
            Screens.SETTINGS -> setScreen(settings)
            Screens.GAME -> setScreen(gameplay)
        }


    }

    fun createState() {
        state = GameState(
            Inventory(),
            GameSettings(),
            SoundEngine()
        )
    }
}