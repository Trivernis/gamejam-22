package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.last.commit.ColorState
import com.last.commit.Game
import com.last.commit.config.ActionCommand

class Settings (val parent: Game)  : TimeTravelScreen() {

    var open = true

    override fun handleKeyInput(action: ActionCommand) {
    }

    override fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int) {
    }

    override fun show() {
        open = true
    }

    private var delta: Float = 0.0f
    val state = ColorState()
    override fun render(delta: Float) {

        this.delta = delta
        state.step((delta * 1000).toLong())
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glClearColor(state.red, state.green, state.blue, 1f)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        open = false
    }

    override fun dispose() {
    }
}