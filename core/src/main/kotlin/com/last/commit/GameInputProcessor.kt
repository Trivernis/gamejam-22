package com.last.commit

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.last.commit.screen.TimeTravelScreen

class GameInputProcessor(val game: Game, ) : InputProcessor{

    val activeScreen = game.screen as TimeTravelScreen

    override fun keyUp(keycode: Int): Boolean {

        //activeScreen.handleKeyInput(keycode)
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        val characterUpperCase = character.uppercase()
        val characterKey = Input.Keys.valueOf(characterUpperCase)

        activeScreen.handleKeyInput(characterKey)
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        activeScreen.handleMouseInput(screenX, screenY, pointer, button)
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}