package com.last.commit

import GameState
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.last.commit.audio.SoundEngine
import com.last.commit.config.GameSettings
import com.last.commit.config.TimeTravelAssetManager
import com.last.commit.inventory.Inventory
import com.last.commit.screen.*
import com.last.commit.map.MapState
import java.awt.AWTEventMulticaster
import java.sql.Time


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

        inputProcessor = GameInputProcessor(this)
        Gdx.input.inputProcessor = inputProcessor

        changeScreen(Screens.MAIN_MENU)
    }

    private fun createScreens() {

        menu = MainMenu(this)
        gameplay = FirstScreen(this)
        settings = Settings(this)
    }

    fun changeScreen(screen : Screens) {
        println("changing screen to $screen")
        when (screen) {
            Screens.MAIN_MENU -> setScreen(menu)
            Screens.SETTINGS -> setScreen(settings)
            Screens.GAME -> setScreen(gameplay)
        }
        inputProcessor.activeScreen = getScreen() as TimeTravelScreen
    }

    fun createState() {
        state = GameState(
            Inventory(),
            GameSettings(),
            SoundEngine(),
            TimeTravelAssetManager()
        )
    }
}