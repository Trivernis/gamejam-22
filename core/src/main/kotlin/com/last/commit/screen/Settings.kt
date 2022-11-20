package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.last.commit.ColorState
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

    var sfxMusicSlider: Slider
    var volumeMusicSlider: Slider
    var musicCheckbox: CheckBox
    var soundEffectsCheckbox: CheckBox
    var backButton:  Button


    val state = ColorState()
    val table = Table()

    init {
        parent.state.assetManager.finishLoading()
        stage = Stage(ScreenViewport())
        skin = parent.state.assetManager.getUiTexture()


        // music volume
        volumeMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        volumeMusicSlider.value = parent.state.settings.musicVolume
        volumeMusicSlider.addListener {
            println("volume slide ${volumeMusicSlider.value}")
            parent.state.settings.musicVolume = volumeMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // sound volume
        sfxMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        sfxMusicSlider.value = parent.state.settings.sfxVolume
        sfxMusicSlider.addListener {

            println("volume sfx :${sfxMusicSlider.value}")
            parent.state.settings.sfxVolume = sfxMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // music on/off
        musicCheckbox = CheckBox(null, skin)
        musicCheckbox.isChecked = !parent.state.settings.musicEnabled
        musicCheckbox.addListener {
            val enabled: Boolean = musicCheckbox.isChecked()
            parent.state.settings.musicEnabled = !enabled
            false
        }

        // sound on/off
        soundEffectsCheckbox = CheckBox(null, skin)
        soundEffectsCheckbox.isChecked = !parent.state.settings.sfxEnabled
        soundEffectsCheckbox.addListener {
            val enabled: Boolean = soundEffectsCheckbox.isChecked()
            parent.state.settings.sfxEnabled = !enabled
            false
        }

        // return to main screen button
        backButton = TextButton("Back", skin)
        backButton.addListener {
            parent.changeScreen(Screens.MAIN_MENU)
            false
        }

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

    }

    fun renderTable(x: Float, y: Float) {
        table.reset()
        table.setFillParent(true)
        //table.setDebug(true);
        stage.addActor(table)



        titleLabel = Label("Preferences", skin)
        volumeMusicLabel = Label("Music Volume", skin)
        volumeSoundLabel = Label("Sound Volume", skin)
        musicOnOffLabel = Label("Music", skin)
        soundOnOffLabel = Label("Sound Effect", skin)
        table.add(titleLabel).colspan(2)
        table.row().pad(10F, 0F, 0F, 10F)
        table.row().size(x, y);
        table.add(volumeMusicLabel).left()
        table.add(volumeMusicSlider)
        table.row().size(x, y);
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(musicOnOffLabel).left()
        table.add(musicCheckbox)
        table.row().size(x, y);
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(volumeSoundLabel).left()
        table.add(sfxMusicSlider)
        table.row().size(x, y);
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(soundOnOffLabel).left()
        table.add(soundEffectsCheckbox)
        table.row().size(x, y);
        table.row().pad(10F, 0F, 0F, 10F)
        table.add(backButton).colspan(2)
    }

    override fun render(delta: Float) {


        state.step((delta * 1000).toLong())
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        var red = MathUtils.clamp(state.red,0.1F, 0.5F)
        var blue = MathUtils.clamp(state.green,0.1F, 0.5F)
        var green = MathUtils.clamp(state.blue,0.1F, 0.5F)

        Gdx.gl.glClearColor(red, green, blue, 1f)


        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.deltaTime, 1 / 30f))
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        println("width $width, height $height")
        stage.viewport.update(width, height, true);


        val y = stage.viewport.screenHeight.toFloat() / 6
        val x = stage.viewport.screenWidth.toFloat() / 4

        renderTable(x, y)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        stage.dispose();
    }
}