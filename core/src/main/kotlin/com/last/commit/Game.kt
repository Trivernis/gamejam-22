package com.last.commit

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.last.commit.audio.SoundEngine
import com.last.commit.config.GameSettings
import com.last.commit.config.TimeTravelAssetManager
import com.last.commit.inventory.Inventory
import com.last.commit.screen.*
import com.last.commit.stages.DialogStage


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    lateinit var state: GameState

    lateinit var menu: TimeTravelScreen
    lateinit var gameplay: TimeTravelScreen
    lateinit var settings: TimeTravelScreen
    val inputProcessors = InputMultiplexer()
    lateinit var inputProcessor: GameInputProcessor

    override fun create() {
        createState()
        createScreens()
        inputProcessor = GameInputProcessor(this)
        gameplay.getInputProcessors().forEach { it -> inputProcessors.addProcessor(it) }
        inputProcessors.addProcessor(inputProcessor)
        Gdx.input.inputProcessor = inputProcessors

        changeScreen(Screens.MAIN_MENU)
    }

    private fun createScreens() {

        menu = MainMenu(this)
        gameplay = FirstScreen(this)
        settings = Settings(this)
    }

    fun changeScreen(screen: Screens) {
        println("changing screen to $screen")
        when (screen) {
            Screens.MAIN_MENU -> setScreen(menu)
            Screens.SETTINGS -> setScreen(settings)
            Screens.GAME -> setScreen(gameplay)
        }
        inputProcessor.activeScreen = getScreen() as TimeTravelScreen
    }

    fun createState() {
        val settings =  GameSettings()
        val soundEngine = SoundEngine(settings.sfxVolume, settings.musicVolume)
        settings.soundEngin = soundEngine
        state = GameState(
            Inventory(),
            settings,
            soundEngine,
            TimeTravelAssetManager(),
            null,
            DialogStage(Skin(Gdx.files.internal("ui/uiskin.json")))
        )
    }
}