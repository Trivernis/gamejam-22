package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.last.commit.ColorState
import com.last.commit.Game
import com.last.commit.config.ActionCommand


class MainMenu(val parent: Game) : TimeTravelScreen() {

    var stage: Stage
    var table = Table()
    val uiSkin: Skin

    init {

        parent.state.assetManager.finishLoading()
        stage = Stage(ScreenViewport())
        uiSkin = parent.state.assetManager.getUiTexture()
    }

    override fun handleKeyInput(action: ActionCommand) {
        parent.changeScreen(Screens.GAME)
    }

    override fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int) {
        stage.touchDown(screenX, screenY, pointer, button)
        stage.touchUp(screenX, screenY, pointer, button)
    }

    override fun getInputProcessors(): Array<InputProcessor> {
        return emptyArray()
    }

    override fun show() {

        stage.addActor(table);


        val y = stage.viewport.screenHeight.toFloat() / 6
        val x = stage.viewport.screenWidth.toFloat() / 4

        println("x: $x, y: $y")

        //add buttons to table
        renderTable(x, y);
    }

    fun renderTable(x: Float, y: Float) {
        table.reset()
        table.setFillParent(true);

        val newGame = TextButton("Play Game", uiSkin)
        val preferences = TextButton("Settings", uiSkin)
        preferences.setSize(stage.viewport.screenWidth.toFloat(), stage.viewport.screenHeight.toFloat())
        val exit = TextButton("Exit", uiSkin)

        table.row().size(x, y);
        table.add(newGame).fillX().uniformX()
        table.row().pad(10F, 0F, 10F, 0F)
        table.row().size(x, y);
        table.add(preferences).fillX().uniformX()
        table.row().size(x, y);
        table.add(exit).fillX().uniformX()

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


    }

    val state = ColorState()
    override fun render(delta: Float) {

        state.step((delta * 1000).toLong())
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glClearColor(state.red, state.green, state.blue, 1f)

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

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