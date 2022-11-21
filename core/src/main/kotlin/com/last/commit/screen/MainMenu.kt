package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.last.commit.ColorState
import com.last.commit.Game
import com.last.commit.TimeTravelAssetManager
import com.last.commit.audio.SoundEngine


class MainMenu(val parent: Game) : Screen {

    var stage = Stage(ScreenViewport())

    lateinit var uiSkin: Skin

    val state = ColorState()

    override fun show() {
        Gdx.input.setInputProcessor(stage);
        SoundEngine.resume()
        uiSkin = TimeTravelAssetManager.getSkin()
        stage.clear()

        //add buttons to table
        createTable();
    }

    fun createTable(): Table {
        var table = Table()
        table.setFillParent(true);
        table.setDebug(true)

        stage.addActor(table);

        val newGame = TextButton("Play Game", uiSkin)

        val preferences = TextButton("Settings", uiSkin)
        val exit = TextButton("Exit", uiSkin)

        exit.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                Gdx.app.exit()
            }
        })

        newGame.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                parent.changeScreen(Screens.GAME)
            }
        })

        preferences.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                parent.changeScreen(Screens.SETTINGS)
            }
        })

        table.add(newGame).fillX().uniformX()
        table.row().pad(10F, 0F, 10F, 0F)
        table.add(preferences).fill().uniformX()
        table.row()
        table.add(exit).fillX().uniformX()

        return table

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
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    override fun resize(width: Int, height: Int) {
        println("width $width, height $height")
        stage.viewport.update(width, height, true);

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