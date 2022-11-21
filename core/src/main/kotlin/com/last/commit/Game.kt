package com.last.commit

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.last.commit.audio.SoundEngine
import com.last.commit.inventory.Inventory
import com.last.commit.screen.*
import com.last.commit.stages.DialogStage


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    lateinit var state: GameState

    lateinit var loading: Screen
    lateinit var menu: Screen
    lateinit var gameplay: Screen
    lateinit var settings: Screen

    lateinit var font: BitmapFont

    override fun create() {
        font = BitmapFont()
        createState()
        createScreens()

        changeScreen(Screens.LOADING)
    }

    private fun createScreens() {
        loading = LoadingScreen(TimeTravelAssetManager, this)
        menu = MainMenu(this)
        gameplay = FirstScreen(this)
        settings = Settings(this)
    }

    fun changeScreen(screen: Screens) {
        println("changing screen to $screen")
        when (screen) {
            Screens.LOADING -> setScreen(loading)
            Screens.MAIN_MENU -> setScreen(menu)
            Screens.SETTINGS -> setScreen(settings)
            Screens.GAME -> setScreen(gameplay)
        }
    }

    fun createState() {
        state = GameState(
            Inventory(),
            SoundEngine,
            null,
            DialogStage(Skin(Gdx.files.internal("ui/uiskin.json")))
        )
    }
}