package com.last.commit

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.last.commit.screen.*


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    lateinit var loading: Screen
    lateinit var menu: Screen
    lateinit var gameplay: Screen
    lateinit var settings: Screen

    lateinit var font: BitmapFont

    override fun create() {
        font = BitmapFont()
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
}