package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.last.commit.Game
import com.last.commit.config.ActionCommand

class Settings(val parent: Game) : TimeTravelScreen() {


    var stage: Stage
    val skin: Skin


    lateinit var titleLabel: Label
    lateinit var volumeMusicLabel: Label
    lateinit var volumeSoundLabel: Label
    lateinit var musicOnOffLabel: Label
    lateinit var soundOnOffLabel: Label

    init {
        parent.state.assetManager.finishLoading()
        stage = Stage(ScreenViewport())
        skin = parent.state.assetManager.getUiTexture()
    }

    override fun handleKeyInput(action: ActionCommand) {
    }

    override fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int) {
        stage.touchDown(screenX, screenY, pointer, button)
        stage.touchUp(screenX, screenY, pointer, button)
    }

    override fun getInputProcessors(): Array<InputProcessor> {
        return emptyArray()
    }


    override fun show() {
        stage.clear()

        // Create a table that fills the screen. Everything else will go inside
        // this table.
        val table = Table()
        table.setFillParent(true)
        //table.setDebug(true);
        stage.addActor(table)


        // music volume
        val volumeMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        volumeMusicSlider.value = parent.state.settings.musicVolume
        volumeMusicSlider.addListener {
            parent.state.settings.musicVolume = volumeMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // sound volume
        val soundMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        soundMusicSlider.value = parent.state.settings.sfxVolume
        soundMusicSlider.addListener {
            parent.state.settings.sfxVolume = soundMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // music on/off
        val musicCheckbox = CheckBox(null, skin)
        musicCheckbox.isChecked = parent.state.settings.musicEnabled
        musicCheckbox.addListener {
            val enabled: Boolean = musicCheckbox.isChecked()
            parent.state.settings.musicEnabled = enabled
            false
        }

        // sound on/off
        val soundEffectsCheckbox = CheckBox(null, skin)
        soundEffectsCheckbox.isChecked = parent.state.settings.sfxEnabled
        soundEffectsCheckbox.addListener {
            val enabled: Boolean = soundEffectsCheckbox.isChecked()
            parent.state.settings.sfxEnabled = enabled
            false
        }

        // return to main screen button
        val backButton = TextButton("Back", skin)
        backButton.addListener {
            parent.changeScreen(Screens.MAIN_MENU)
            false
        }
        titleLabel = Label("Preferences", skin)
        volumeMusicLabel = Label("Music Volume", skin)
        volumeSoundLabel = Label("Sound Volume", skin)
        musicOnOffLabel = Label("Music", skin)
        soundOnOffLabel = Label("Sound Effect", skin)
        table.add(titleLabel).colspan(2)
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(volumeMusicLabel).left()
        table.add(volumeMusicSlider)
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(musicOnOffLabel).left()
        table.add(musicCheckbox)
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(volumeSoundLabel).left()
        table.add(soundMusicSlider)
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(soundOnOffLabel).left()
        table.add(soundEffectsCheckbox)
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(backButton).colspan(2)
    }

    override fun render(delta: Float) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.deltaTime, 1 / 30f))
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
    }
}