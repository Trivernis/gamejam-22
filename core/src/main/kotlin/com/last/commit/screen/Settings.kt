package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.last.commit.ColorState
import com.last.commit.Game
import com.last.commit.TimeTravelAssetManager
import com.last.commit.audio.SoundEngine

class Settings(val parent: Game) : Screen {

    val stage = Stage(ScreenViewport())
    lateinit var skin: Skin


    lateinit var titleLabel: Label
    lateinit var volumeMusicLabel: Label
    lateinit var volumeSoundLabel: Label
    lateinit var musicOnOffLabel: Label
    lateinit var soundOnOffLabel: Label

    lateinit var sfxMusicSlider: Slider
    lateinit var volumeMusicSlider: Slider
    lateinit var musicCheckbox: CheckBox
    lateinit var soundEffectsCheckbox: CheckBox
    lateinit var backButton: Button


    val state = ColorState()
    val table = Table()

    override fun show() {
        stage.clear()
        Gdx.input.setInputProcessor(stage)
        skin = TimeTravelAssetManager.getSkin()


        // music volume
        volumeMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        volumeMusicSlider.value = SoundEngine.musicVolume
        volumeMusicSlider.addListener {
            SoundEngine.musicVolume = volumeMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // sound volume
        sfxMusicSlider = Slider(0f, 1f, 0.1f, false, skin)
        sfxMusicSlider.value = SoundEngine.sfxVolume
        sfxMusicSlider.addListener {

            SoundEngine.sfxVolume = sfxMusicSlider.value
            // updateVolumeLabel();
            false
        }

        // music on/off
        musicCheckbox = CheckBox(null, skin)
        musicCheckbox.isChecked = SoundEngine.musicVolume > 0
        musicCheckbox.addListener {
            val enabled: Boolean = musicCheckbox.isChecked()
            //TODO: reimplement this
            SoundEngine.musicVolume = if (enabled) 0.5f else 0f
            false
        }

        // sound on/off
        soundEffectsCheckbox = CheckBox(null, skin)
        soundEffectsCheckbox.isChecked = SoundEngine.sfxVolume > 0
        soundEffectsCheckbox.addListener {
            val enabled: Boolean = soundEffectsCheckbox.isChecked()
            //TODO: reimplement this
            SoundEngine.sfxVolume = if (enabled) 0.5f else 0f
            false
        }

        // return to main screen button
        backButton = TextButton("Back", skin)
        backButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                parent.changeScreen(Screens.MAIN_MENU)
            }
        })

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
        var red = MathUtils.clamp(state.red, 0.1F, 0.5F)
        var blue = MathUtils.clamp(state.green, 0.1F, 0.5F)
        var green = MathUtils.clamp(state.blue, 0.1F, 0.5F)

        Gdx.gl.glClearColor(red, green, blue, 1f)


        // tell our stage to do actions and draw itself
        stage.act(Math.min(delta, 1 / 30f))
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